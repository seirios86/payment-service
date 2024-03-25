package com.cardpaymentsystem.paymentservice.util;

import java.security.MessageDigest;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

@Component
public class EncryptionUtil {

	private static final String HASH_ALGORITHM = "SHA-256";
	private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";
	private static final String AES = "AES";
	private static final String AES_KEY = "FlskypDkZ2OVoBB+A81b4sQ7nlFq5XmSRLEm760pfww=";
	private static final String IV = "CxUti5Xr1HVpw2W0RfEdjg==";

	public String hash(String plainText) throws Exception {

		MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
		byte[] hash = digest.digest(plainText.getBytes());

		return Base64.getEncoder().encodeToString(hash);
	}

	public String encrypt(String plainText) throws Exception {

		return encrypt(plainText, IV);
	}

	public String encrypt(String plainText, String iv) throws Exception {

		SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.getDecoder().decode(AES_KEY), AES);
		IvParameterSpec ivSpec = new IvParameterSpec(Base64.getDecoder().decode(iv));
		Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);
		byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());

		return Base64.getEncoder().encodeToString(encryptedBytes);
	}
	
	public String decrypt(String encrypted) throws Exception {

		return decrypt(encrypted, IV);
	}

	public String decrypt(String encrypted, String iv) throws Exception {

		SecretKeySpec secretKeySpec = new SecretKeySpec(Base64.getDecoder().decode(AES_KEY), AES);
		IvParameterSpec ivSpec = new IvParameterSpec(Base64.getDecoder().decode(iv));
		Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivSpec);
		byte[] decodedBytes = Base64.getDecoder().decode(encrypted);
		byte[] decryptedBytes = cipher.doFinal(decodedBytes);

		return new String(decryptedBytes);
	}
}
