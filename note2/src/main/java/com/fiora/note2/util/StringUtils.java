package com.fiora.note2.util;

import java.io.UnsupportedEncodingException;

public class StringUtils {
    public static String gbk2uft8(String str) {
        try {
            byte[] temp = str.getBytes("gbk");
            return new String(temp, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
