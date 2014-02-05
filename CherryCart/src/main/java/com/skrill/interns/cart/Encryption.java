package com.skrill.interns.cart;

import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class Encryption {

    private Cipher cipher;
    private static SecretKeySpec secretKey;
    private static IvParameterSpec ivSpec;
    private static byte[] keyBytes = new byte[16];
    private static byte[] iv = new byte[16];
    private static String key = "12aADFrfafERefa@332adfaERfre$dr343afarWAERAVAR##$$%DFeWfr43";

    public Encryption() {
        try {
            this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        generateKeyAndIv();
        secretKey = new SecretKeySpec(keyBytes, "AES");
        ivSpec = new IvParameterSpec(iv);
    }

    private static void generateKeyAndIv() {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(key.getBytes());
            String encryptedKey = new String(messageDigest.digest());
            for (int i = 0; i < keyBytes.length; i++) {
                keyBytes[i] = encryptedKey.getBytes()[i];
            }
            for (int i = 0; i < iv.length; i++) {
                iv[i] = encryptedKey.getBytes()[encryptedKey.length() - i - 1];
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public byte[] encrypt(String message) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            byte[] encrypted = cipher.doFinal(message.getBytes(Charset.forName("UTF-8")));

            return Base64.encodeBase64(encrypted);
        } catch (InvalidKeyException e) {
            System.out.println("SecretKey is invalid!");
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            System.out.println("Illegal Block Size");
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            System.out.println("Invalid initialization vector");
            e.printStackTrace();
        }
        return null;
    }

    public String decrypt(byte[] encrypted) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            byte[] decrypted = cipher.doFinal(Base64.decodeBase64(encrypted));
            return new String(decrypted);
        } catch (InvalidKeyException e) {
            System.out.println("SecretKey is invalid!");
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            System.out.println("Illegal Block Size");
            e.printStackTrace();
        } catch (BadPaddingException e) {
            System.out.println("Bad Padding");
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            System.out.println("Invalid initialization vector");
            e.printStackTrace();
        }
        return null;
    }
}
