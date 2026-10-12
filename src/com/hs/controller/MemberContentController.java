package com.hs.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hs.model.BoardDAO;
import com.hs.model.MemberVO;
import com.hs.model.CryptVO;
import com.hs.util.AesCrypt;
import com.hs.util.KeyFile;

public class MemberContentController implements Controller {

	@Override
	public String requestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String ctx = request.getContextPath();
		HttpSession session = request.getSession(false);
		String loginId = (String) session.getAttribute("userId");
		boolean admin = "A".equals(session.getAttribute("userAuth"));
		String listPage = "redirect:" + ctx + (admin ? "/boardAdmin.do" : "/boardList.do");
		String userId = request.getParameter("userId");
		if(userId == null || (!admin && !userId.equals(loginId))) { // 자신의 정보이거나 관리자일 때만 조회 가능
			return listPage;
		}
		BoardDAO dao = new BoardDAO();
		MemberVO vo = dao.memberContent(userId);
		if(vo == null) {
			return listPage;
		}
		try {
			String[] key = KeyFile.load(request); // WAS에 파일로 존재하는 k2, iv2
			CryptVO cvo = dao.dataCrypt(); // db의 Enc(k1)
			vo.setEmail(new AesCrypt().Decrypt(vo.getEmail(), key[0], key[1], cvo.getK2()));
		} catch (Exception e) {
			throw new ServletException("not content", e);
		}
		vo.setPass(null); // 비밀번호 해시는 화면으로 넘기지 않는다.
		request.setAttribute("m_vo", vo);
		return "memberContent";
	}
}
