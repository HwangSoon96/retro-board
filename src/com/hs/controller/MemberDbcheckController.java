package com.hs.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hs.model.BoardDAO;

public class MemberDbcheckController implements Controller {

	@Override
	public String requestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String id = request.getParameter("id"); // 중복체크 누르면 넘어오는 id 데이터
		
		BoardDAO dao = new BoardDAO();
		String dbDouble = dao.memberDbcheck(id); // YES or NO 가 들어있다.
		
		response.getWriter().print(dbDouble); // ajax 함수에 만들어 놓은 callback 함수(dbCheck)로 응답이 된다.
		return null; // 다른 페이지로 forward 되거나 redirect 되면 안되므로 위에서 response로 바로 응답해버리고 여기서는 null 값을 리턴한다.
	}

}
