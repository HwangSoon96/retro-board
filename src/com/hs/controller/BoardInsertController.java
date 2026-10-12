package com.hs.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hs.model.BoardDAO;
import com.hs.model.BoardVO;

public class BoardInsertController implements Controller {

	@Override
	public String requestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String ctx = request.getContextPath();
		HttpSession session = request.getSession(false);
		String title = request.getParameter("title");
		String contents = request.getParameter("contents");
		if(title == null || contents == null) {
			throw new ServletException("not insert");
		}
		// 작성자와 권한은 클라이언트가 보낸 값이 아닌 세션에서 가져온다.
		String writer = (String) session.getAttribute("userId");
		String auth = (String) session.getAttribute("userAuth");
		BoardVO vo = new BoardVO();
		vo.setTitle(title);
		vo.setContents(contents);
		vo.setCount(0);
		vo.setWriter(writer);
		BoardDAO dao = new BoardDAO();
		int cnt = dao.boardInsert(vo);
		if(cnt > 0) {
			if("A".equals(auth)) {
				return "redirect:" + ctx + "/boardAdmin.do";
			}
			return "redirect:" + ctx + "/boardList.do";
		}
		throw new ServletException("not insert");
	}
}
