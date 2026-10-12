package com.hs.util;

import java.security.SecureRandom;

public class CsrfToken {

	// 로그인할 때 세션에 저장하고, 데이터를 바꾸는 요청마다 폼으로 함께 받아 비교한다.
	public static String create() {
		byte[] token = new byte[32];
		new SecureRandom().nextBytes(token);
		return ConvertHex.byteToHex(token);
	}
}
