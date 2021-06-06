package com.fiora.note2.book;

import com.fiora.note2.util.FastExcelUtil;
import java.util.List;

public class DealWithBook {
    public static final String EXCEL_PATH = "D:/拥有的书目.xlsx";
    public static final String EXCEL_PATH2 = "D:/拥有的书目2.xlsx";
    public static final String JSON_PATH = "D:/拥有的书目.json";
    public static void main(String[] args) {
        List<BookEntity> list = readFromExcel();
        writeToExcel(list);
    }

    public static List<BookEntity> readFromExcel() {
        List<BookEntity> list = FastExcelUtil.getListFromExcel(EXCEL_PATH, BookEntity.class);
        System.out.println(list);
        return list;
    }

    public static void writeToExcel(List<BookEntity> list) {
        for (BookEntity book : list) {
            System.out.print(book.getBookName()+"\t");
            System.out.print(book.getPublisher()== null ? "" :book.getPublisher()+"\t");
            System.out.println();
        }
    }
}
