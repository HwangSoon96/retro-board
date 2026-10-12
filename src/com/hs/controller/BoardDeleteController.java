package com.hs.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hs.model.BoardDAO;
import com.hs.model.BoardVO;

public class BoardDeleteController implements Controller {

	@Override
	public String requestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String ctx = request.getContextPath();
		HttpSession session = request.getSession(false);
		String userId = (String) session.getAttribute("userId");
		boolean admin = "A".equals(session.getAttribute("userAuth"));
		String listPage = "redirect:" + ctx + (admin ? "/boardAdmin.do" : "/boardList.do");
		int idx = Integer.parseInt(request.getParameter("bno"));
		BoardDAO dao = new BoardDAO();
		BoardVO vo = dao.boardContent(idx);
		if(vo == null || (!admin && !vo.getWriter().equals(userId))) { // 관리자이거나 자신이 쓴 글일 때만 삭제 가능
			return listPage;
		}
		int cnt = dao.boardDelete(idx);
		if(cnt > 0) {
			return listPage;
		}
		throw new ServletException("not delete");
	}
}
