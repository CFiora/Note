package com.fiora.note2.book;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class FileTest {
    public static void main(String[] args) {
        String path_prefix = "I:\\0V\\ad漫画\\wuyaowang\\comic\\180";
        String path = "I:\\0V\\ad漫画\\wuyaowang\\comic\\180\\180_%03d_%s";
        File file = new File(path_prefix);
        File[] directories = file.listFiles();
        for (File directory : directories) {
            int intDirectory = Integer.parseInt(directory.getName());
            File[] pics = directory.listFiles();
            Arrays.stream(pics).forEach(pic -> {
                try {
                    FileUtils.copyFile(pic, new File(String.format(path, intDirectory, pic.getName())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
