package com.fiora.note2.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.dhatim.fastexcel.reader.Cell;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.elasticsearch.common.recycler.Recycler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FastExcelUtil {
    private static String filePathPrefix = "D:\\1500T\\";
    private static String[] files = {"2018更新资源0.xlsx", "2019更新资源0.xlsx", "2020更新资源0.xlsx", "KTV0.xlsx", "百家0.xlsx", "电视剧0.xlsx",
            "电影0.xlsx", "动漫0.xlsx", "高清经典0.xlsx", "更新资源0.xlsx", "纪录片0.xlsx",
            "教育0.xlsx", "蓝光专区0.xlsx", "体育0.xlsx", "图书0.xlsx", "音乐0.xlsx",
            "游戏IT0.xlsx", "中美百万0.xlsx", "综艺0.xlsx"};
    //    private static String file = "音乐0.xlsx";//"图书0.xlsx","音乐0.xlsx"
    private static String Url = "jdbc:mysql://localhost:3306/mysql?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8";//参数参考MySql连接数据库常用参数及代码示例
    private static String name = "root";//数据库用户名
    private static String psd = "root";//数据库密码
    private static String jdbcName = "com.mysql.cj.jdbc.Driver";//连接MySql数据库
    private static String sql = "insert into net_disk_1(`code`,`name`,`path`,`size`,`time`,`md5`,`directory`) values(?,?,?,?,?,?,'0')";
    private static int batchSize = 10000;

    public static void main(String[] args) {
//        simpleTest();
    }

    private static void countSize() {
        String filePathPrefix = "D:\\百度网盘目录\\";
        String[] files = {"小九-图书.xlsx", "小九-知识付费.xlsx", "防爆文件夹.xlsx"};
        JSONArray resultJson = new JSONArray();
        for (String file : files) {
            JSONObject fileJson = new JSONObject();
            fileJson.put("文件名", file);
            int total = 0; BigDecimal unit = BigDecimal.valueOf(1024);
            BigDecimal kb = BigDecimal.ZERO;BigDecimal mb = BigDecimal.ZERO;BigDecimal gb = BigDecimal.ZERO;
            try (InputStream is = new FileInputStream(filePathPrefix + file); ReadableWorkbook wb = new ReadableWorkbook(is)) {
                BigDecimal skb = BigDecimal.ZERO;BigDecimal smb = BigDecimal.ZERO;BigDecimal sgb = BigDecimal.ZERO;
                long sheets = wb.getSheets().count();
                JSONArray data = new JSONArray();
                for(long j=0;j<sheets; j++) {
                    Optional<Sheet> optional = wb.getSheet(Long.valueOf(j).intValue());
                    Sheet sheet = optional.get();
                    JSONObject sheetData = new JSONObject();
                    sheetData.put("sheet名称", sheet.getName());
                    List<Row> rows = sheet.read();
                    if (rows.size() == 0) {
                        sheetData.put("大小(MB)", 0);
                        sheetData.put("数量", 0);
                        continue;
                    }
                    total += rows.size();
                    for (int i = 2; i < rows.size(); i++) {
                        System.out.println("文件" + file + "第" + (i + 1) + "行记录,总数第" + rows.size() + "行记录");
                        String strSize = rows.get(i).getCell(3).getText();
                        if(strSize.endsWith("kb")) {
                            kb = kb.add(BigDecimal.valueOf(Double.valueOf(strSize.replace("kb",""))));
                            skb = skb.add(BigDecimal.valueOf(Double.valueOf(strSize.replace("kb",""))));
                        } else if (strSize.endsWith("M")) {
                            mb = mb.add(BigDecimal.valueOf(Double.valueOf(strSize.replace("M",""))));
                            smb = smb.add(BigDecimal.valueOf(Double.valueOf(strSize.replace("M",""))));
                        } else if (strSize.endsWith("G")) {
                            gb = gb.add(BigDecimal.valueOf(Double.valueOf(strSize.replace("G",""))));
                            sgb = sgb.add(BigDecimal.valueOf(Double.valueOf(strSize.replace("G",""))));
                        }
                    }
                    BigDecimal sResult = sgb.multiply(unit).add(smb).add(skb.divide(unit, RoundingMode.HALF_UP));
                    sheetData.put("大小(MB)", sResult.toString());
                    sheetData.put("数量", rows.size());
                    data.add(sheetData);
                }
                BigDecimal result = gb.multiply(unit).add(mb).add(kb.divide(unit, RoundingMode.HALF_UP));
                fileJson.put("data",data);
                fileJson.put("大小（MB）",result.toString());
                fileJson.put("文件数量", total);
                resultJson.add(fileJson);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(resultJson);
    }

    private static void bigExcel2DB() {
        try {
            int total = 0;
            Class.forName(jdbcName);//向DriverManager注册自己
            Connection con = DriverManager.getConnection(Url, name, psd);//与数据库建立连接
            con.setAutoCommit(false);
            PreparedStatement pst = con.prepareStatement(sql);
            for (String file : files) {
                try (InputStream is = new FileInputStream(filePathPrefix + file); ReadableWorkbook wb = new ReadableWorkbook(is)) {
                    Sheet sheet = wb.getFirstSheet();
                    List<Row> rows = sheet.read();
                    for (int i = 2; i < rows.size(); i++) {
                        total++;
                        System.out.println("文件" + file + "第" + (i + 1) + "行记录,总数第" + total + "行记录");
                        Row row = rows.get(i);
                        pst.setString(1, row.getCell(0) == null ? "" : row.getCell(0).getText());
                        pst.setString(2, row.getCell(1) == null ? "" : row.getCell(1).getText());
                        pst.setString(3, row.getCell(2) == null ? "" : row.getCell(2).getText());
                        pst.setString(4, row.getCell(3) == null ? "" : row.getCell(3).getText());
                        pst.setString(5, row.getCell(4) == null ? "" : row.getCell(4).getText());
                        pst.setString(6, row.getCell(5) == null ? "" : row.getCell(5).getText());
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
                }
            }

            pst.close();
            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static <T> List<T> getListFromExcel(String filePath, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        try (InputStream is = new FileInputStream(filePath); ReadableWorkbook wb = new ReadableWorkbook(is)) {
            Sheet sheet = wb.getFirstSheet();
            List<Row> rows = sheet.read();
            Row title = rows.get(0);
            List<String> fields =title.stream().map((cell) -> cell.getText()).filter(field -> !field.trim().isEmpty()).collect(Collectors.toList());
            System.out.println(fields);
            for (int i = 2; i < rows.size(); i++) {
                Row row = rows.get(i);
                int columns = fields.size();
                T instance = clazz.newInstance();
                boolean ifAddedToList = false;
                for(int j = 0; j < columns; j++) {
                    Object value = null;
                    try {
                        Cell cell = row.getCell(j);
                        value = cell.getValue();
                    } catch (Exception e) {
                        // Do nothing
                    }
                    if (value != null) {
                        ifAddedToList = true;
                    }
                    String field = fields.get(j);
                    Field f = instance.getClass().getDeclaredField(field);
                    f.setAccessible(true);
                    f.set(instance, value);
                }
                if(ifAddedToList) {
                    list.add(instance);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    private static void simpleTest() {
        String filePathPrefix = "D:\\百度网盘目录\\";
//        String[] files = {"图书0.xlsx","音乐0.xlsx"};
        String file = "防爆文件夹.xlsx";
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
            Row rowl2 = rows.get(rows.size() - 2);
            printOut(rowl2);
            Row rowl1 = rows.get(rows.size() - 1);
            printOut(rowl1);

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
