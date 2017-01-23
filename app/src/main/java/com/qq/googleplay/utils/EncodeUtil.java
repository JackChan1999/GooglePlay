package com.qq.googleplay.utils;

import org.apache.commons.codec.binary.Base64;

public class EncodeUtil {
    public static byte[] urlsafeEncodeBytes(byte[] src) {
        if (src.length % 3 == 0) {
            return encodeBase64Ex(src);
        }
        byte[] b = encodeBase64Ex(src);
        if (b.length % 4 == 0) {
            return b;
        }
        int pad = 4 - (b.length % 4);
        byte[] b2 = new byte[(b.length + pad)];
        System.arraycopy(b, 0, b2, 0, b.length);
        b2[b.length] = (byte) 61;
        if (pad > 1) {
            b2[b.length + 1] = (byte) 61;
        }
        return b2;
    }

    private static byte[] encodeBase64Ex(byte[] src) {
        byte[] b64 = Base64.encodeBase64(src);
        for (int i = 0; i < b64.length; i++) {
            if (b64[i] == (byte) 47) {
                b64[i] = (byte) 95;
            } else if (b64[i] == (byte) 43) {
                b64[i] = (byte) 45;
            }
        }
        return b64;
    }
}
