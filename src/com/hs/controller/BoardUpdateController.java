package com.hs.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hs.model.BoardDAO;
import com.hs.model.BoardVO;

public class BoardUpdateController implements Controller {

	@Override
	public String requestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String ctx = request.getContextPath();
		HttpSession session = request.getSession(false);
		String userId = (String) session.getAttribute("userId");
		int bno = Integer.parseInt(request.getParameter("idx"));
		String title = request.getParameter("title");
		String contents = request.getParameter("contents");
		BoardDAO dao = new BoardDAO();
		BoardVO saved = dao.boardContent(bno);
		if(saved == null || title == null || contents == null || !saved.getWriter().equals(userId)) { // 자신이 쓴 글만 수정 가능
			return "redirect:" + ctx + "/boardList.do";
		}
		BoardVO vo = new BoardVO();
		vo.setIdx(bno);
		vo.setTitle(title);
		vo.setContents(contents);
		int cnt = dao.boardUpdate(vo);
		if(cnt > 0) {
			return "redirect:" + ctx + "/boardContent.do?bno=" + bno + "&mode=update";
		}
		throw new ServletException("not update");
	}
}
