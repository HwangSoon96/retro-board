package com.hs.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MemberRegisterController implements Controller {

	@Override
	public String requestHandler(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		return "memberRegister"; // 뷰의 이름만 리턴 -> 앞뒤로 생략된 경로는 prefix와 suffix를 붙여 완성 -> ViewResolver
		
	}

}
