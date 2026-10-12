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
import com.hs.util.CryptPass;
import com.hs.util.KeyFile;

public class MemberInsertController implements Controller {

	private static final String ID_PATTERN = "^[A-Za-z0-9]{4,20}$";

	@Override
	public String requestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String ctx = request.getContextPath();
		String id = request.getParameter("id");
		String pass = request.getParameter("pass");
		String name = request.getParameter("name");
		String email = request.getParameter("email");
		int age = -1;
		try {
			age = Integer.parseInt(request.getParameter("age"));
		} catch (NumberFormatException e) {
			age = -1;
		}
		// 화면에서 한 검사는 우회할 수 있기 때문에 서버에서 다시 검사한다.
		if(id == null || !id.matches(ID_PATTERN)) {
			return fail(request, "아이디는 영문과 숫자 4~20자로 입력해주세요.");
		}
		if(pass == null || pass.length() < 4 || name == null || name.trim().isEmpty()
				|| email == null || email.trim().isEmpty() || age < 0) {
			return fail(request, "입력한 정보를 다시 확인해주세요.");
		}
		BoardDAO dao = new BoardDAO();
		if("YES".equals(dao.memberDbcheck(id))) {
			return fail(request, "중복된 아이디 입니다");
		}
		String enc_email = "";
		try {
			String[] key = KeyFile.load(request); // WAS에 파일로 존재하는 k2, iv2
			CryptVO cvo = dao.dataCrypt(); // db의 Enc(k1)
			enc_email = new AesCrypt().Encrypt(email, key[0], key[1], cvo.getK2());
		} catch (Exception e) {
			throw new ServletException("not insert", e); // 암호화에 실패하면 평문이나 빈 값을 저장하지 않고 가입을 중단한다.
		}
		MemberVO vo = new MemberVO();
		vo.setId(id);
		vo.setPass(CryptPass.hash(pass));
		vo.setName(name);
		vo.setAge(age);
		vo.setEmail(enc_email);
		vo.setAuth("U"); // 가입은 항상 일반유저. 권한을 폼에서 받지 않는다.
		int cnt = dao.memberInsert(vo);
		if(cnt > 0) {
			HttpSession session = request.getSession();
			session.setAttribute("msg_register", "회원가입이 완료되었습니다. 로그인 해주세요.");
			return "redirect:" + ctx + "/boardLogin.do";
		}
		throw new ServletException("not insert");
	}

	private String fail(HttpServletRequest request, String message) {
		request.getSession().setAttribute("msg_register_error", message);
		return "redirect:" + request.getContextPath() + "/memberRegister.do";
	}
}
