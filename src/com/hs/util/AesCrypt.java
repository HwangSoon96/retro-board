package com.hs.util;

import java.security.SecureRandom;

public class AesCrypt {

	private static final int IV_BYTES = 16;
	private static final int IV_HEX_LENGTH = IV_BYTES * 2;

	// db의 Enc(k1)을 WAS의 k2, iv2로 복호화해서 k1을 획득
	private String dataKey(String k2, String iv2, String enc_k1) throws Exception {
		Aes_CBC_PKCS7 aes = new Aes_CBC_PKCS7(k2, iv2);
		return aes.AesCBCDecode(enc_k1);
	}

	// 암호화할 때마다 새 IV를 만들고 암호문 앞에 붙여서 저장한다. (같은 이메일이라도 암호문이 달라진다.)
	public String Encrypt(String data, String k2, String iv2, String enc_k1) throws Exception {
		String k1 = dataKey(k2, iv2, enc_k1);
		byte[] iv = new byte[IV_BYTES];
		new SecureRandom().nextBytes(iv);
		Aes_CBC_PKCS7 aes = new Aes_CBC_PKCS7(k1, iv);
		return ConvertHex.byteToHex(iv) + aes.AesCBCEncode(data);
	}

	// 암호문 앞 32자(16바이트)가 IV, 나머지가 실제 암호문
	public String Decrypt(String data, String k2, String iv2, String enc_k1) throws Exception {
		String k1 = dataKey(k2, iv2, enc_k1);
		byte[] iv = ConvertHex.hexToByte(data.substring(0, IV_HEX_LENGTH));
		Aes_CBC_PKCS7 aes = new Aes_CBC_PKCS7(k1, iv);
		return aes.AesCBCDecode(data.substring(IV_HEX_LENGTH));
	}
}
