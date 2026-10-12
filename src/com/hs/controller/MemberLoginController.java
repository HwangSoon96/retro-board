package com.hs.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hs.model.BoardDAO;
import com.hs.model.MemberVO;
import com.hs.util.CryptPass;
import com.hs.util.CsrfToken;

public class MemberLoginController implements Controller {

	@Override
	public String requestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String ctx = request.getContextPath();
		String user_id = request.getParameter("user_id");
		String password = request.getParameter("password");
		BoardDAO dao = new BoardDAO();
		// 아이디로 회원을 찾은 다음, 저장된 솔트로 입력한 비밀번호를 다시 계산해서 비교한다.
		MemberVO vo = user_id == null ? null : dao.memberContent(user_id);
		if(vo != null && CryptPass.verify(password, vo.getPass())) {
			HttpSession session = request.getSession();
			request.changeSessionId(); // 로그인 전에 쓰던 세션 아이디를 버린다. (세션 고정 방어)
			session.setAttribute("userId", vo.getId());
			session.setAttribute("userAuth", vo.getAuth()); // 권한레벨 U : 일반유저 / A : 관리자
			session.setAttribute("csrfToken", CsrfToken.create());
			if("A".equals(vo.getAuth())) {
				return "redirect:" + ctx + "/boardAdmin.do";
			}
			return "redirect:" + ctx + "/boardList.do";
		}
		request.getSession().setAttribute("msg_login", "사용자 정보가 올바르지 않습니다.");
		return "redirect:" + ctx + "/boardLogin.do";
	}
}
