package com.coeta.group5.chat_bot_ai.utils;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class AESUtil {

    private static final String SECRET_KEY = "e+4E3zQ51BVMUnhpRrUkO8FsXhR1djjgFx4+Jw71Hes="; // must match frontend

    public static String decrypt(String encryptedBase64, String ivHex) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(SECRET_KEY);
        byte[] ivBytes = hexStringToByteArray(ivHex); // IV in hex from React
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedBase64);

        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

        byte[] original = cipher.doFinal(encryptedBytes);
        return new String(original, "UTF-8");
    }

    // Helper to convert hex to byte[]
    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] output = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            output[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return output;
    }
}
