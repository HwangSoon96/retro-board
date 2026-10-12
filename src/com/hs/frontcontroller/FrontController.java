package com.hs.frontcontroller;

import javax.servlet.http.HttpSession;
import java.security.MessageDigest;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hs.controller.*;

@WebServlet("*.do")
public class FrontController extends HttpServlet {  // FrontController는 해야하는 일이 정해져 있다.(수정할 일 거의 없음)
														  // 클라 요청 파악 -> 핸들러맵핑 통해 알맞은 POJO 찾고 일 시키기 -> redirect or forward 구분
														  // Spring에서는 DispatcherServlet = FrontController
	protected void service(HttpServletRequest request, HttpServletResponse response) 
												throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8"); // 클라이언트에서 넘어온 데이터 중 한글 데이터를 깨지지 않게 하기 위해서 설정
		
		// FrontController -> 클라이언트가 어떤 요청을 했는지 파악하기
		String url = request.getRequestURI(); // 클라이언트가 요청한 url을 문자열로 받는다.
		// System.out.println(url); // 포트번호 뒤의 경로 가져온다. (/MVC04/memberList.do)
		
		String ctx = request.getContextPath(); // Context 경로 가져오기
		// System.out.println(ctx); // /MVC04
		
		// ctx 길이부터 시작하는 클라이언트 요청 url (우리가 필요한 진짜 클라이언트의 요청)
		String command = url.substring(ctx.length());
		// System.out.println(command); // /memberInsert.do
		
		Controller controller = null;
		String nextPage = null;
		
		// 핸들러 맵핑을 따로 클래스를 만들어 HashMap으로 구현
		HandlerMapping mapping = new HandlerMapping();
		controller = mapping.getController(command); // 요청을 넘겨줘서 그에 맞는 POJO를 리턴
		if(controller == null) { // 등록되지 않은 요청
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		// 권한 검사를 화면(JSP)이 아닌 여기서 한 번에 한다. 모든 요청이 FrontController를 거치기 때문에 빠지는 요청이 없다.
		HttpSession session = request.getSession(false);
		String userId = session == null ? null : (String) session.getAttribute("userId");
		String userAuth = session == null ? null : (String) session.getAttribute("userAuth");
		int level = mapping.getLevel(command);
		if(level != HandlerMapping.PUBLIC) {
			response.setHeader("Cache-Control", "no-store"); // 로그아웃 후 뒤로 가기로 이전 화면이 보이지 않게 브라우저에 저장하지 않는다.
			boolean login = userId != null && !"".equals(userId) && userAuth != null;
			if(!login || (level == HandlerMapping.ADMIN && !"A".equals(userAuth))) {
				request.getSession().setAttribute("msg_login", "잘못된 접근입니다. 다시 로그인해주세요.");
				response.sendRedirect(ctx + "/boardLogin.do");
				return;
			}
		}

		// 데이터를 바꾸는 요청은 POST만 받고, 로그인할 때 세션에 넣어둔 토큰과 폼으로 넘어온 토큰이 같은지 확인한다. (CSRF 방어)
		if(mapping.isPostOnly(command)) {
			if(!"POST".equals(request.getMethod())) {
				response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
				return;
			}
			String token = session == null ? null : (String) session.getAttribute("csrfToken");
			String sent = request.getParameter("_csrf");
			if(token == null || sent == null || !MessageDigest.isEqual(token.getBytes("UTF-8"), sent.getBytes("UTF-8"))) {
				response.sendError(HttpServletResponse.SC_FORBIDDEN);
				return;
			}
		}

		nextPage = controller.requestHandler(request, response); // POJO에게 일을 시키고, nextPage에 대한 정보를 받는다.
		
		// forward인지 redirect인지 구분하기
		// redirect인 경우 경로 앞에 redirect: 를 붙여 구분해준다. 후에 : 을 기준으로 뒤쪽을 다시 경로로 설정해주는 과정을 거치면 된다.
		// 이러면 위 if-else 구문들에 있는 중복된 구문들을 줄일 수 있다.
		if(nextPage != null) {
			if(nextPage.indexOf("redirect:") != -1) { // 만약 경로에 redirect가 있다면
				// nextPage = "redirect:/MVC04/memberList.do"
				response.sendRedirect(nextPage.split(":")[1]); // redirect, : 기준으로 나눠서 뒤쪽 경로 가져온다.
			} else {
				// forward의 경우 view의 이름만 리턴 -> 각각의 POJO에서 경로를 직접 적어주면, 경로에 변경사항이 생길 시 해당 POJO들을 전부 수정해야 한다
				// 그러나 이렇게 경로의 논리적인 이름 앞뒤로 prefix와 suffix를 붙여주면 경로에 변경사항이 생겨도 여기만 수정해주면 된다. -> ViewResolver
				// RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/member/" + nextPage + ".jsp"); // forward
				
				// ViewResolver
				RequestDispatcher rd = request.getRequestDispatcher(ViewResolver.makeView(nextPage)); // forward
				rd.forward(request, response);
			}
		}
		
		
		// ----- 핸들러 매핑 부분을 남기고 redirect, forward 부분은 중복이 되므로 따로 if 문으로 빼버려서 핸들러 매핑 부분만 남긴 것. -----
		/* 
		// 요청에 따른 구분 작업 (if~ else if~)
		if(command.equals("/memberList.do")) { // 멤버 리스트 화면
			controller = new MemberListController();
			nextPage = controller.requestHandler(request, response); // POJO에게 일을 시키고, nextPage에 대한 정보를 받는다.
			// RequestDispatcher rd = request.getRequestDispatcher(nextPage);
			// rd.forward(request, response);
		} else if(command.equals("/memberInsert.do")) { // 회원 가입
			controller = new MemberInsertController();
			nextPage = controller.requestHandler(request, response);
			// response.sendRedirect("/MVC04/memberList.do"); // 가입 성공 시 서버가 응답을 memberList.do로 돌리는 기술 = 리다이렉트
		} else if(command.equals("/memberRegister.do")) { // 회원 가입 화면
			controller = new MemberRegisterController();
			nextPage = controller.requestHandler(request, response);
			// RequestDispatcher rd = request.getRequestDispatcher(nextPage); // 리다이렉트로 하면 경로가 해당 경로로 바뀌어 버린다.
			// rd.forward(request, response); // forward로 해야 뭔가 잡다한 url 변경이 없이 컨트롤러가 일 처리하기 쉬울듯
		} else if(command.equals("/memberContent.do")) { // 회원 상세 보기 화면
			controller = new MemberContentController();
			nextPage = controller.requestHandler(request, response);
			// forward
			// RequestDispatcher rd = request.getRequestDispatcher(nextPage);
			// rd.forward(request, response);
		} else if(command.equals("/memberUpdate.do")) { // 회원 정보 수정
			controller = new MemberUpdateController();
			nextPage = controller.requestHandler(request, response);
			// response.sendRedirect(nextPage); // 수정 성공 시 서버가 응답을 memberList.do로 돌리는 기술 = 리다이렉트
		} else if(command.equals("/memberDelete.do")) { // 회원 삭제
			controller = new MemberDeleteController();
			nextPage = controller.requestHandler(request, response);
			// response.sendRedirect(nextPage); // 멤버 리스트 화면으로 리다이렉트
		} // if end
		*/
		
		
		// ----- FrontController가 모든 요청을 받아 모든 일을 처리 -----
		/*
		// 요청에 따른 구분 작업 (if~ else if~)
		if(command.equals("/memberList.do")) { // 멤버 리스트 화면
			MemberDAO dao = new MemberDAO();
			List<MemberVO> list = dao.memberList();
			request.setAttribute("list", list);
			RequestDispatcher rd = request.getRequestDispatcher("/member/memberList.jsp");
			rd.forward(request, response);
		} else if(command.equals("/memberInsert.do")) { // 회원 가입
			// 1. 클라이언트에서 넘어오는 파라미터 수집 (MemberVo에 하나의 객체로 묶어주면 된다.)
			String id = request.getParameter("id");
			String pass = request.getParameter("pass");
			String name = request.getParameter("name");
			int age = Integer.parseInt(request.getParameter("age"));
			String email = request.getParameter("email");
			String phone = request.getParameter("phone");
			
			// MemberVO vo = new MemberVO(id, pass, name, age, email, phone);
			
			// 위에는 내가 편하게 생성자 메서드를 고려해서 만들었을 경우고, 일반적으로는 밑에 객체 생성하고 세터 메서드로 MemberVO에 파리미터 수집해준다.
			MemberVO vo = new MemberVO();
			vo.setId(id);
			vo.setPass(pass);
			vo.setName(name);
			vo.setAge(age);
			vo.setEmail(email);
			vo.setPhone(phone);
			
			// System.out.println(vo.toString());
			
			// MemberVO를 Model과 연동
			MemberDAO dao = new MemberDAO();
			int cnt = dao.memberInsert(vo);
			
			// PrintWriter out = response.getWriter();
			if(cnt > 0) {
				// 가입 성공
				// out.println("insert success");
				response.sendRedirect("/MVC04/memberList.do"); // 가입 성공 시 서버가 응답을 memberList.do로 돌리는 기술 = 리다이렉트
			} else {
				// 가입 실패 -> 예외객체를 만들어서 WAS에게 던짐
				throw new ServletException("not insert");
			}
			
		} else if(command.equals("/memberRegister.do")) { // 회원 가입 화면
			RequestDispatcher rd = request.getRequestDispatcher("/member/memberRegister.html"); // 리다이렉트로 하면 경로가 해당 경로로 바뀌어 버린다.
			rd.forward(request, response); // forward로 해야 뭔가 잡다한 url 변경이 없이 컨트롤러가 일 처리하기 쉬울듯
		} else if(command.equals("/memberContent.do")) { // 회원 상세 보기 화면
			// url로 넘어오는 파리미터를 수집
			int num = Integer.parseInt(request.getParameter("num"));
			
			MemberDAO dao = new MemberDAO();
			MemberVO vo = dao.memberContent(num);
			
			// 객체 바인딩
			request.setAttribute("vo", vo);
			
			// forward
			RequestDispatcher rd = request.getRequestDispatcher("member/memberContent.jsp");
			rd.forward(request, response);
		} else if(command.equals("/memberUpdate.do")) { // 회원 정보 수정
			// 클라이언트에서 넘어온 파라미터 수집
			int num = Integer.parseInt(request.getParameter("num"));
			int age = Integer.parseInt(request.getParameter("age"));
			String email = request.getParameter("email");
			String phone = request.getParameter("phone");
			
			MemberVO vo = new MemberVO();
			vo.setNum(num);
			vo.setAge(age);
			vo.setEmail(email);
			vo.setPhone(phone);
			
			MemberDAO dao = new MemberDAO();
			int cnt = dao.memberUpdate(vo);
			
			if(cnt > 0) {
				// 수정 성공
				response.sendRedirect("/MVC04/memberList.do"); // 수정 성공 시 서버가 응답을 memberList.do로 돌리는 기술 = 리다이렉트
			} else {
				// 수정 실패 -> 예외객체를 만들어서 WAS에게 던짐
				throw new ServletException("not update");
			}
		} else if(command.equals("/memberDelete.do")) { // 회원 삭제
			// 클라이언트에서 http://localhost:8080/MVC01/memberDelete.do?num=7 이런 식으로 요청이 들어온다.
			int num = Integer.parseInt(request.getParameter("num")); // num 받아서 해당 회원 삭제 메서드 사용한다.
			
			MemberDAO dao = new MemberDAO();
			int cnt = dao.memberDelete(num);
			if(cnt > 0) {
				response.sendRedirect("/MVC04/memberList.do"); // 멤버 리스트 화면으로 리다이렉트
			} else {
				throw new ServletException("not delete");
			}
		} // if end
		*/
		
	}

}
