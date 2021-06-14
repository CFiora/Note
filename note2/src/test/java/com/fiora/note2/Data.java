package com.fiora.note2;

import com.alicloud.openservices.tablestore.model.BatchWriteRowRequest;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class Data {
    @Test
    public void getRows() throws IOException {
        String filePathPrefix = "D:\\百度网盘目录\\";
        String[] files = {"G.【全网影音】 - 副本.xlsx"};
        int total = 0;
        for (String file : files) {
            try (InputStream is = new FileInputStream(filePathPrefix + file); ReadableWorkbook wb = new ReadableWorkbook(is)) {
                long sheets = wb.getSheets().count();
                for(long j=0;j<sheets; j++) {
                    Optional<Sheet> optional = wb.getSheet(Long.valueOf(j).intValue());
                    Sheet sheet = optional.get();
                    List<Row> rows = sheet.read();
                    int subTotal = rows.size() - 2;
                    total += subTotal;
                    System.out.println(String.format("共有数据%d条,sheet %d 共有数据%d条", total, j, subTotal));
                }
            }
        }

    }

    public static void main(String[] args) {
        test2();
    }

    private static void test2() {
        BatchWriteRowRequest batchWriteRowRequest  = new BatchWriteRowRequest();
        System.out.println(batchWriteRowRequest);
        if(true) {
            batchWriteRowRequest  = new BatchWriteRowRequest();
            System.out.println(batchWriteRowRequest);
        }
        System.out.println(batchWriteRowRequest);
    }
}
