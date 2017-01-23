package com.qq.googleplay.utils;

import com.qq.googleplay.common.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * ============================================================
 * <p/>
 * 版 权 ： Google互联网有限公司版权所有 (c) 2016
 * <p/>
 * 作 者 : 陈冠杰
 * <p/>
 * 版 本 ： 1.0
 * <p/>
 * 创建日期 ： 2016-2-21 下午6:16:40
 * <p/>
 * 描 述 ：
 * md5工具类
 * <p/>
 * 修订历史 ：
 * <p/>
 * ============================================================
 **/
public class MD5Util {

    public static byte[] encode(byte[] bytes) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(bytes);
            return digest.digest();
        } catch (Exception e) {
            return null;
        }
    }

    public static String encode2HexStr(byte[] bytes) {
        return HexUtil.bytes2HexStr(encode(bytes));
    }

    public static String encodeHexStr(String value) {
        try {
            return HexUtil.bytes2HexStr(encode(value.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encode(String password) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            byte[] digest = instance.digest(password.getBytes());

            StringBuffer sb = new StringBuffer();
            for (byte b : digest) {
                int i = b & 0xff;
                String hexString = Integer.toHexString(i);
                if (hexString.length() < 2) {
                    hexString = "0" + hexString;
                }
                sb.append(hexString);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getFileMd5(String sourceDir) {

        File file = new File(sourceDir);
        try {
            FileInputStream fis = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len = -1;
            // 获取到数字摘要
            MessageDigest messageDigest = MessageDigest.getInstance("md5");
            while ((len = fis.read(buffer)) != -1) {
                messageDigest.update(buffer, 0, len);
            }
            byte[] result = messageDigest.digest();
            StringBuffer sb = new StringBuffer();
            for (byte b : result) {
                int number = b & 0xff; // 加盐 +1 ;
                String hex = Integer.toHexString(number);
                if (hex.length() == 1) {
                    sb.append("0" + hex);
                } else {
                    sb.append(hex);
                }
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String md5sum(String filename) {
        try {
            return md5sum(new FileInputStream(filename));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String md5sum(InputStream is) {
        String toHexString;
        byte[] buffer = new byte[1024];
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            while (true) {
                int numRead = is.read(buffer, 0, 1024);
                if (numRead <= 0) {
                    break;
                }
                md5.update(buffer, 0, numRead);
            }
            toHexString = CommonUtil.toHexString(md5.digest());
        } catch (Exception e) {
            toHexString = "";
        } finally {
            IOUtils.closeQuietly(is);
        }
        return toHexString;
    }

    public static String getMD5(String message) {
        if (message == null) {
            return "";
        }
        return getMD5(message.getBytes());
    }

    public static String getMD5(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        String digest = "";
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(bytes);
            return CommonUtil.toHexString(algorithm.digest());
        } catch (Exception e) {
            return digest;
        }
    }
}
