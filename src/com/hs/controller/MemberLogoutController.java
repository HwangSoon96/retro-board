package com.hs.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MemberLogoutController implements Controller {

	@Override
	public String requestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String ctx = request.getContextPath();
		
		// 현재 세션 가져와서 세션 끊어버리기
		// 1. 강제로 끊어버리기
		request.getSession().invalidate();
		// 2. 브라우저 종료
		// 3. 세션이 종료될 때까지 기다리기 (로그인 후 일정 시간이 지나면 자동 로그아웃, 세션 타임아웃(기본 30분))
		
		return "redirect:" + ctx + "/boardLogin.do";
	}

}
