package com.fiora.note2.util;

import java.util.Random;

public class IDUtil {
    public static String getNetDiskId() {
        int random = new Random().nextInt(900000) + 100000;
        return System.currentTimeMillis()+ "" + random;
    }

    public static void main(String[] args) {
        System.out.println(getNetDiskId());
    }
}
