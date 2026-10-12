package com.hs.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hs.model.BoardDAO;
import com.hs.model.BoardVO;
import com.hs.model.MemberVO;

public class BoardAdminController implements Controller {

	@Override
	public String requestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		BoardDAO dao = new BoardDAO();
		List<BoardVO> b_list = dao.boardList();
		request.setAttribute("b_list", b_list);
		List<MemberVO> m_list = dao.memberList();
		request.setAttribute("m_list", m_list);
		return "boardAdmin";
	}
}
