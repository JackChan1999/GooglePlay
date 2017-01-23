package com.qq.googleplay.utils;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public abstract class Coder {
    public static final String HMAC_SHA1 = "HmacSHA1";
    public static final String KEY_MD5 = "MD5";
    public static final String KEY_SHA = "SHA-1";

    public static byte[] encryptBASE64(byte[] key) throws Exception {
        return Base64.encodeBase64(key);
    }

    public static byte[] decryptBASE64(String key) throws Exception {
        return Base64.decodeBase64(key.getBytes());
    }

    public static byte[] encryptMD5(byte[] data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(data);
        return md5.digest();
    }

    public static String encryptMD5(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(byteArray[i] & 255).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(byteArray[i] & 255));
            } else {
                md5StrBuff.append(Integer.toHexString(byteArray[i] & 255));
            }
        }
        return md5StrBuff.toString();
    }

    public static byte[] encryptSHA(byte[] data) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        sha.update(data);
        return sha.digest();
    }

    public static String initMacKey() throws Exception {
        return getString(encryptBASE64(KeyGenerator.getInstance("HmacSHA1").generateKey().getEncoded()));
    }

    public static byte[] encryptHMAC(byte[] data, String key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA1");
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(secretKey);
        return mac.doFinal(data);
    }

    public static String encryptHMAC2String(String data, String key) throws Exception {
        return getString(encryptHMAC(data.getBytes(), key));
    }

    public static String getString(byte[] b) {
        StringBuffer sb = new StringBuffer();
        for (byte append : b) {
            sb.append(append);
        }
        return sb.toString();
    }
}
