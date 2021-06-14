package com.fiora.note2.util;

import com.fiora.note2.model.NetDisk;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ExcelUtil {

    public static Object getCellFormatValue(Cell cell) {
        if(cell!=null){
            if( Cell.CELL_TYPE_STRING == cell.getCellType()) {
                return cell.getRichStringCellValue().getString();
            }
        }
        return "";
    }

    public static void assignNetDisk(NetDisk net, Row row) {
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
