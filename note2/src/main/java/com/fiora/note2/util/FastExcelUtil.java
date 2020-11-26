package com.fiora.note2.util;

import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.List;

public class FastExcelUtil {
    private static String filePathPrefix = "D:\\1500T\\";
    private static String file = "音乐0.xlsx";//"图书0.xlsx","音乐0.xlsx"
    private static String Url = "jdbc:mysql://localhost:3306/mysql?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8";//参数参考MySql连接数据库常用参数及代码示例
    private static String name = "root";//数据库用户名
    private static String psd = "root";//数据库密码
    private static String jdbcName = "com.mysql.cj.jdbc.Driver";//连接MySql数据库
    private static String sql = "insert into net_disk_1(`code`,`name`,`path`,`size`,`time`,`md5`,`directory`) values(?,?,?,?,?,?,'0')";
    private static int batchSize = 15000;

    public static void main(String[] args) {
        bigExcel2DB();
    }

    private static void bigExcel2DB() {
        try {
            Class.forName(jdbcName);//向DriverManager注册自己
            Connection con = DriverManager.getConnection(Url, name, psd);//与数据库建立连接
            con.setAutoCommit(false);
            PreparedStatement pst = con.prepareStatement(sql);
            try (InputStream is = new FileInputStream(filePathPrefix + file); ReadableWorkbook wb = new ReadableWorkbook(is)) {
                Sheet sheet = wb.getFirstSheet();
                List<Row> rows = sheet.read();
                for (int i = 2; i < rows.size(); i++) {
//                for (int i = 2; i < 30050; i++) {
                    System.out.println("第" + (i + 1) + "行记录");
                    Row row = rows.get(i);
                    pst.setString(1, row.getCell(0).getText());
                    pst.setString(2, row.getCell(1).getText());
                    pst.setString(3, row.getCell(2).getText());
                    pst.setString(4, row.getCell(3).getText());
                    pst.setString(5, row.getCell(4).getText());
                    pst.setString(6, row.getCell(5).getText());
                    pst.addBatch();
                    if (i % batchSize == 0) {//可以设置不同的大小；如50，100，500，1000等等
                        pst.executeBatch();
                        con.commit();
                        pst.clearBatch();
                    }
                }
                if (rows.size() % batchSize != 0) {//可以设置不同的大小；如50，100，500，1000等等
                    pst.executeBatch();
                    con.commit();
                    pst.clearBatch();
                }
                pst.close();
                con.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void simpleTest() {
        String filePathPrefix = "D:\\1500T\\";
//        String[] files = {"图书0.xlsx","音乐0.xlsx"};
        String file = "图书0.xlsx";
        try (InputStream is = new FileInputStream(filePathPrefix + file); ReadableWorkbook wb = new ReadableWorkbook(is)) {
            Sheet sheet = wb.getFirstSheet();
            List<Row> rows = sheet.read();
            System.out.println(rows.size());
            Row row0 = rows.get(0);
            printOut(row0);
            Row row1 = rows.get(1);
            printOut(row1);
            Row row2 = rows.get(2);
            printOut(row2);
            Row row3 = rows.get(3);
            printOut(row3);
            Row rowl2 = rows.get(rows.size()-2);
            printOut(rowl2);
            Row rowl1 = rows.get(rows.size()-1);
            printOut(rowl1);

//            try (Stream<Row> rows = sheet.openStream()) {
//                rows.forEach(r -> {
//                    System.out.println(r.getCellCount());
//                });
//            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void printOut(Row row) {
        row.forEach(cell -> {
            System.out.print(cell.getText() + "\t");
        });
        System.out.println();
    }
}
