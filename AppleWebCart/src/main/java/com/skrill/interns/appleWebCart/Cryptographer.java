/*
 * $ Kaloyan Milkov, Atanas Kereziev, Stanislav Burov$
 *
 * Copyright <2013> Moneybookers Ltd. All Rights Reserved.
 * MONEYBOOKERS PROPRIETARY/CONFIDENTIAL. For internal use only.
 */
package com.skrill.interns.appleWebCart;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class Cryptographer {

    private final String KEY_STRING = "Apple";
    private final int BLOCK_SIZE = 16;
    private Cipher encryptCipher;
    private Cipher decryptCipher;
    private SecretKey key;
    private IvParameterSpec iv;

    public Cryptographer() {
        try {
            setup();
            encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key, iv);
            decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            decryptCipher.init(Cipher.DECRYPT_MODE, key, iv);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    public void setup() {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(KEY_STRING.getBytes());

            byte[] keyHash = messageDigest.digest();
            byte[] keyArray = new byte[BLOCK_SIZE];
            System.arraycopy(keyHash, 0, keyArray, 0, BLOCK_SIZE);
            byte[] ivArray = new byte[BLOCK_SIZE];
            System.arraycopy(keyHash, BLOCK_SIZE, ivArray, 0, BLOCK_SIZE);

            key = new SecretKeySpec(keyArray, "AES");
            iv = new IvParameterSpec(ivArray);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String encrypt(String message) {

        byte[] encrypted = null;
        try {
            byte[] input = message.getBytes("UTF-8");
            encrypted = encryptCipher.doFinal(input);
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return Base64.encodeBase64URLSafeString(encrypted);
    }

    public String decrypt(String message) throws IllegalBlockSizeException {

        byte[] encrypted = Base64.decodeBase64(message);
        byte[] decrypted = null;

        try {
            decrypted = decryptCipher.doFinal(encrypted);
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        String result = null;
        try {
            result = new String(decrypted, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
