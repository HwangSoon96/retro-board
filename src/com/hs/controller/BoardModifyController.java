package com.hs.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hs.model.BoardDAO;
import com.hs.model.BoardVO;

public class BoardModifyController implements Controller {

	@Override
	public String requestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String ctx = request.getContextPath();
		HttpSession session = request.getSession(false);
		String userId = (String) session.getAttribute("userId");
		int bno = Integer.parseInt(request.getParameter("bno"));
		BoardDAO dao = new BoardDAO();
		BoardVO vo = dao.boardContent(bno);
		if(vo == null || !vo.getWriter().equals(userId)) { // 자신이 쓴 글만 수정 화면으로 갈 수 있다.
			return "redirect:" + ctx + "/boardList.do";
		}
		request.setAttribute("b_vo", vo);
		return "boardModify";
	}
}
