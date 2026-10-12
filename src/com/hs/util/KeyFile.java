package com.hs.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

public class KeyFile {

	// WAS의 WEB-INF/key.txt에서 k2(첫 줄)와 iv2(둘째 줄)를 읽는다.
	public static String[] load(HttpServletRequest request) throws IOException {
		String path = request.getServletContext().getRealPath("") + File.separator + "WEB-INF" + File.separator + "key.txt";
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(path)))) {
			String k2 = reader.readLine();
			String iv2 = reader.readLine();
			if (k2 == null || iv2 == null) {
				throw new IOException("key.txt에 k2와 iv2가 없습니다.");
			}
			return new String[] { k2.trim(), iv2.trim() };
		}
	}
}
