package com.hs.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hs.model.BoardDAO;

public class MemberDeleteController implements Controller {

	@Override
	public String requestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String ctx = request.getContextPath();
		int num = Integer.parseInt(request.getParameter("num"));
		String id = request.getParameter("id");
		BoardDAO dao = new BoardDAO();
		int cnt = dao.memberDelete(num);
		if(cnt > 0) {
			if(id != null) {
				dao.memberDeleteAll(id); // 해당 회원의 모든 게시물 삭제
			}
			return "redirect:" + ctx + "/boardAdmin.do";
		}
		throw new ServletException("not delete");
	}
}
