package com.fiora.note2.util;

import com.fiora.note2.model.NetDisk1;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ExcelUtil {
    //读取excel 小文件
    public static Workbook readExcel(String filePath){
        Workbook wb = null;
        if(filePath==null){
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if(".xls".equals(extString)){
                return wb = new HSSFWorkbook(is);
            }else if(".xlsx".equals(extString)){
                return wb = new XSSFWorkbook(is);
            }else{
                return wb = null;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }

    public static Object getCellFormatValue(Cell cell) {
        if(cell!=null){
            if( Cell.CELL_TYPE_STRING == cell.getCellType()) {
                return cell.getRichStringCellValue().getString();
            }
        }
        return "";
    }

//    public static Object getCellFormatValue(Cell cell){
//        Object cellValue = null;
//        if(cell!=null){
//            //判断cell类型
//            switch(cell.getCellType()){
//                case Cell.CELL_TYPE_STRING:{
//                    cellValue = cell.getRichStringCellValue().getString();
//                    break;
//                }
//                case Cell.CELL_TYPE_NUMERIC:{
//                    cellValue = String.valueOf(cell.getNumericCellValue());
//                    break;
//                }
//                case Cell.CELL_TYPE_FORMULA:{
//                    //判断cell是否为日期格式
//                    if(DateUtil.isCellDateFormatted(cell)){
//                        //转换为日期格式YYYY-mm-dd
//                        cellValue = cell.getDateCellValue();
//                    }else{
//                        //数字
//                        cellValue = String.valueOf(cell.getNumericCellValue());
//                    }
//                    break;
//                }
//                default:
//                    cellValue = "";
//            }
//        }else{
//            cellValue = "";
//        }
//        return cellValue;
//    }

    public static void assignNetDisk(NetDisk1 net, Row row) {
        //获取最大列数
        // int colnum = row.getPhysicalNumberOfCells();
        // String columns[] = {"文件编码", "文件名称", "文件路径", "文件md5", "大小", "修改时间"};
        net.setCode((String) ExcelUtil.getCellFormatValue(row.getCell(0)));
        net.setName((String) ExcelUtil.getCellFormatValue(row.getCell(1)));
        net.setPath((String) ExcelUtil.getCellFormatValue(row.getCell(2)));
        net.setMd5((String) ExcelUtil.getCellFormatValue(row.getCell(3)));
        net.setSize((String) ExcelUtil.getCellFormatValue(row.getCell(4)));
        net.setTime((String) ExcelUtil.getCellFormatValue(row.getCell(5)));
    }

}
