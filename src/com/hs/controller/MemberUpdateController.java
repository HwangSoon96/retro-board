package com.hs.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hs.model.BoardDAO;
import com.hs.model.MemberVO;

public class MemberUpdateController implements Controller {

	@Override
	public String requestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String ctx = request.getContextPath();
		
		// 클라이언트에서 넘어온 파라미터 수집
		int num = Integer.parseInt(request.getParameter("num"));
		int age = Integer.parseInt(request.getParameter("age"));
		
		MemberVO vo = new MemberVO();
		
		vo.setNum(num);
		vo.setAge(age);
		
		BoardDAO dao = new BoardDAO();
		
		int cnt = -1;
		cnt = dao.memberUpdate(vo);
		
		String nextPage = null;
		if(cnt > 0) {
			// 수정 성공
			nextPage = "redirect:" + ctx + "/memberList.do";
		} else {
			// 수정 실패 -> 예외객체를 만들어서 WAS에게 던짐
			throw new ServletException("not update");
		}
		
		return nextPage;
	}

}
