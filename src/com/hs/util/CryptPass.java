package com.hs.util;

import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class CryptPass {

	private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
	private static final int ITERATIONS = 120000; // 반복 횟수가 클수록 무차별 대입에 시간이 오래 걸린다.
	private static final int SALT_BYTES = 16;
	private static final int KEY_BITS = 256;

	// 회원마다 무작위 솔트를 만들어 "pbkdf2$반복횟수$솔트$해시" 형태로 반환한다.
	public static String hash(String password) {
		byte[] salt = new byte[SALT_BYTES];
		new SecureRandom().nextBytes(salt);
		byte[] hash = pbkdf2(password, salt, ITERATIONS);
		return "pbkdf2$" + ITERATIONS + "$" + ConvertHex.byteToHex(salt) + "$" + ConvertHex.byteToHex(hash);
	}

	// 저장된 값에서 반복 횟수와 솔트를 꺼내 같은 방식으로 계산한 뒤 비교한다.
	public static boolean verify(String password, String stored) {
		if (password == null || stored == null) {
			return false;
		}
		String[] parts = stored.split("\\$");
		if (parts.length != 4 || !"pbkdf2".equals(parts[0])) {
			return false;
		}
		try {
			int iterations = Integer.parseInt(parts[1]);
			byte[] salt = ConvertHex.hexToByte(parts[2]);
			byte[] expected = ConvertHex.hexToByte(parts[3]);
			if (salt == null || expected == null) {
				return false;
			}
			byte[] actual = pbkdf2(password, salt, iterations);
			return MessageDigest.isEqual(expected, actual); // 일치 여부와 상관없이 걸리는 시간이 같은 비교
		} catch (NumberFormatException e) {
			return false;
		}
	}

	private static byte[] pbkdf2(String password, byte[] salt, int iterations) {
		try {
			PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, KEY_BITS);
			return SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).getEncoded();
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}
}
