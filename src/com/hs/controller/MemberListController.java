package com.hs.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hs.model.BoardDAO;
import com.hs.model.MemberVO;

public class MemberListController implements Controller {

	@Override
	public String requestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// POJO한테 최대한 많은 일을 준다 -> Model과 연동 + 객체 바인딩 + 마지막 다음 페이지에 대한 정보를 FrontController한테 리턴
		// 이렇게 하면 FrontController는 JSP로 forward만 하면 된다.
		BoardDAO dao = new BoardDAO();
		List<MemberVO> list = dao.memberList();
		request.setAttribute("list", list);
		
		// return "/WEB-INF/member/memberList.jsp"; // 기존 WebContent 밑에 그냥 view가 있으면
												 // http://localhost:8080/MVC04/member/memberRegister.html으로 클라이언트가 바로 view로 요청 가능
												 // 그러나 MVC에서 클라 요청은 컨트롤러만 받아야 하므로 view를 url 접근 불가능한 WEB-INF로 옮겨 요청 방지
												 // forward는 url이 바뀌지 않으므로 url 상에 WEB-INF가 나타나지 않아 그냥 사용 가능
		
		return "memberList"; // 뷰의 이름만 리턴 -> 앞뒤로 생략된 경로는 prefix와 suffix를 붙여 완성 -> ViewResolver
		
	}

}
