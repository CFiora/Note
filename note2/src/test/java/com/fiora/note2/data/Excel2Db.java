package com.fiora.note2.data;

import com.fiora.note2.model.NetDisk1;
import com.fiora.note2.util.ExcelUtil;
import org.apache.poi.ss.usermodel.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Excel2Db {

//    private static String filePathPrefix = "D:\\1500T外\\";
//    private static String[] files = {"100T精品教程2.xlsx", "600T.xlsx", "1500T补充.xlsx"};
//    private static String[] files = {"鬼刀.xlsx"};
    private static String filePathPrefix = "D:\\1500T\\";
    private static String[] files = {"2018更新资源0.xlsx","2019更新资源0.xlsx","KTV0.xlsx","百家0.xlsx","电视剧0.xlsx",
            "电影0.xlsx","动漫0.xlsx","高清经典0.xlsx","更新资源0.xlsx","纪录片0.xlsx",
            "教育0.xlsx","蓝光专区0.xlsx","体育0.xlsx","图书0.xlsx","音乐0.xlsx",
            "游戏IT0.xlsx","中美百万0.xlsx","综艺0.xlsx"};
//    private static String[] files = {"2020更新资源0.xlsx"};
    private static String Url = "jdbc:mysql://localhost:3306/mysql?serverTimezone=UTC&useUnicode=true&characterEncoding=utf8";//参数参考MySql连接数据库常用参数及代码示例
    private static String name = "root";//数据库用户名
    private static String psd = "root";//数据库密码
    private static String jdbcName = "com.mysql.cj.jdbc.Driver";//连接MySql数据库
    private static String sql = "insert into net_disk_1(`code`,`name`,`path`,`size`,`time`,`md5`,`directory`) values(?,?,?,?,?,?,'0')";
    private static int batchSize = 5000;

    public static void main(String[] args) {
        excel2db();
    }


    public static void excel2db() {
        try {
            Class.forName(jdbcName);//向DriverManager注册自己
            Connection con = DriverManager.getConnection(Url, name, psd);//与数据库建立连接
            con.setAutoCommit(false);
            PreparedStatement pst = con.prepareStatement(sql);
            for (String file : files) {
                String filePath = filePathPrefix + file;
                Workbook wb = ExcelUtil.readExcel(filePath);
                if(wb != null){
                    //获取第一个sheet
                    Sheet sheet = wb.getSheetAt(0);
                    //获取最大行数
                    int rownum = sheet.getPhysicalNumberOfRows();
                    //获取第3行
                    for (int i = 2; i < rownum; i++) {
                        System.out.println("第" + (i + 1) + "行记录");
                        Row row = sheet.getRow(i);
                        if(row !=null){
                            pst.setString(1, (String) ExcelUtil.getCellFormatValue(row.getCell(1)));
                            pst.setString(2, (String) ExcelUtil.getCellFormatValue(row.getCell(2)));
                            pst.setString(3, (String) ExcelUtil.getCellFormatValue(row.getCell(3)));
                            pst.setString(4, (String) ExcelUtil.getCellFormatValue(row.getCell(4)));
                            pst.setString(5, (String) ExcelUtil.getCellFormatValue(row.getCell(5)));
                            pst.setString(6, (String) ExcelUtil.getCellFormatValue(row.getCell(6)));
                            pst.addBatch();
                        } else {
                            break;
                        }
                        if (i % batchSize == 0 || i == rownum-1) {//可以设置不同的大小；如50，100，500，1000等等
                            pst.executeBatch();
                            con.commit();
                            pst.clearBatch();
                        }
                    }
                }
            }
            pst.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void countMaxLength() {
        int maxNameSize = 0;
        int maxPathSize = 0;
        String maxNameFile = "";
        String maxPathFile = "";
        int maxNameRow = 0;
        int maxPathRow = 0;
        String maxNameValue = "";
        String maxPathValue = "";
        for (String file : files) {
            String filePath = filePathPrefix + file;
            Workbook wb = ExcelUtil.readExcel(filePath);
            if(wb != null){
                //获取第一个sheet
                Sheet sheet = wb.getSheetAt(0);
                //获取最大行数
                int rownum = sheet.getPhysicalNumberOfRows();
                //获取第3行
                for (int i = 2; i < rownum; i++) {
                    System.out.println("文件" + file + "第" + (i + 1) + "行记录");
                    Row row = sheet.getRow(i);
                    if(row !=null){
                        String name = (String) ExcelUtil.getCellFormatValue(row.getCell(1));
                        if (name.length() >  maxNameSize) {
                            maxNameSize = name.length();
                            maxNameFile = file;
                            maxNameValue = name;
                            maxNameRow = i+1;

                        }
                        String path = (String) ExcelUtil.getCellFormatValue(row.getCell(2));
                        if (path.length() >  maxPathSize) {
                            maxPathSize = path.length();
                            maxPathFile = file;
                            maxPathValue = path;
                            maxPathRow = i+1;
                        }
                    } else {
                        break;
                    }
                }
            }
        }
        System.out.println("最长的name是在"+maxNameFile+"中的第"+maxNameRow+"行，value=\r\n"+maxNameValue+"\r\n长度是=="+maxNameSize);
        System.out.println("最长的path是在"+maxPathFile+"中的第"+maxPathRow+"行，value=\n"+maxPathValue+"\r\n长度是=="+maxPathSize);
    }

    public static void divideTest() {
        int i = 2003;
        int size = 1000;
        System.out.println(i%size);
    }

    public static void excelTest() {
        Workbook wb =null;
        Sheet sheet = null;
        Row row = null;
        List<Map<String,String>> list = null;
        String cellData = null;
        String filePath = "F:\\2020更新资源0.xlsx";
        String columns[] = {"文件编码","文件名称","文件路径","文件md5","大小","修改时间"};
        wb = ExcelUtil.readExcel(filePath);
        if(wb != null){
            //用来存放表中数据
            list = new ArrayList<Map<String,String>>();
            //获取第一个sheet
            sheet = wb.getSheetAt(0);
            //获取最大行数
            int rownum = sheet.getPhysicalNumberOfRows();
            //获取第3行
            row = sheet.getRow(2);
            //获取最大列数
            int colnum = 6;
            for (int i = 2; i<5; i++) {
                Map<String,String> map = new LinkedHashMap<String,String>();
                row = sheet.getRow(i);
                if(row !=null){
                    for (int j=0;j<colnum;j++){
                        cellData = (String) ExcelUtil.getCellFormatValue(row.getCell(j));
                        map.put(columns[j], cellData);
                    }
                }else{
                    break;
                }
                list.add(map);
            }
        }
        //遍历解析出来的list
        for (Map<String,String> map : list) {
            for (Map.Entry<String,String> entry : map.entrySet()) {
                System.out.print(entry.getKey()+":"+entry.getValue()+",");
            }
            System.out.println();
        }
    }
}
