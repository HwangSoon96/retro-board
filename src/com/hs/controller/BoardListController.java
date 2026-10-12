package com.hs.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hs.model.BoardDAO;
import com.hs.model.BoardVO;

public class BoardListController implements Controller {

	@Override
	public String requestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		BoardDAO dao = new BoardDAO();
		List<BoardVO> list = dao.boardList();
		request.setAttribute("b_list", list); // 저장된 값을 그대로 넘기고, 화면에서 <c:out>으로 이스케이프해서 출력한다.
		return "boardList";
	}
}
