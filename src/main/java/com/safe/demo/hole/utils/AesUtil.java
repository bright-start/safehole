package com.safe.demo.hole.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * aes加密工具
 *
 * @author: xiongk@tcsec.com.cn
 * @create: 2018-09-28 08:42
 */
public class AesUtil {

	private static final String TYPE = "AES";

	private static final String ALGORITHM = "AES/ECB/PKCS5Padding";

	private static final byte[] key = "ASTPASTPASTPASTP".getBytes(Charset.forName("UTF-8"));
	/**
	 * Encrypts the given plain text
	 *
	 * @param plainText The plain text to encrypt
	 */
	public static byte[] encrypt(byte[] plainText) throws Exception {

		SecretKeySpec secretKey = new SecretKeySpec(key, TYPE);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);

		return cipher.doFinal(plainText);
	}


	/**
	 * Decrypts the given byte array
	 *
	 * @param cipherText The data to decrypt
	 */
	public static byte[] decrypt(byte[] cipherText) throws Exception {
		SecretKeySpec secretKey = new SecretKeySpec(key, TYPE);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, secretKey);

		return cipher.doFinal(cipherText);
	}

	/**
	 * 根据key设置种子，根据种子进行随机生成一个新key
	 * @param
	 * @return
	 * @throws Exception
	 */
	public static SecretKeySpec RandKey() throws NoSuchAlgorithmException {

		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed("t7CLWhtUTAgAFbw0".getBytes());
		keyGenerator.init(128, secureRandom);
		SecretKey secretKey = keyGenerator.generateKey();
		byte[] enCodeFormat = secretKey.getEncoded();
		return new SecretKeySpec(enCodeFormat, "AES");
	}

	public static byte[] encryptRand(byte[] plainText) throws Exception {

		SecretKeySpec secretKeySpec = RandKey();
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

		return cipher.doFinal(plainText);
	}

	public static byte[] decryptRand(byte[] cipherText) throws Exception {
		SecretKeySpec secretKeySpec = RandKey();
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

		return cipher.doFinal(cipherText);
	}

	public static void main(String[] args) throws Exception {
//		byte[] bytes = AesUtil.encrypt("你好".getBytes());
//		System.out.println(bytes);

	}
}
