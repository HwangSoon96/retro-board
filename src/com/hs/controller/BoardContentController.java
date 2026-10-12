package com.hs.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hs.model.BoardDAO;
import com.hs.model.BoardVO;

public class BoardContentController implements Controller {

	@Override
	public String requestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String ctx = request.getContextPath();
		int bno = Integer.parseInt(request.getParameter("bno"));
		String mode = request.getParameter("mode");
		BoardDAO dao = new BoardDAO();
		dao.boardCount(bno, mode);
		BoardVO vo = dao.boardContent(bno);
		if(vo == null) { // 없는 게시물
			return "redirect:" + ctx + "/boardList.do";
		}
		request.setAttribute("b_vo", vo);
		return "boardContent";
	}
}
