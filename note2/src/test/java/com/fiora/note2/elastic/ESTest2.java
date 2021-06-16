package com.fiora.note2.elastic;

import com.fiora.note2.dao.NetDiskESRepository;
import com.fiora.note2.model.NetDisk;
import net.sf.json.JSONObject;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
import org.elasticsearch.search.sort.ScoreSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.APPEND;

@SpringBootTest
public class ESTest2 {

    @Autowired
    private NetDiskESRepository repository;

    @Test
    public void pageTest() {
        String filter = "公务员";
        List<Sort.Order> orders = new ArrayList();
        orders.add(new Sort.Order(Sort.Direction.DESC,"_score"));
        for (int i = 0; i < 4; i++) {
            Page<NetDisk> page = repository.findByNameLikeOrPathLike(filter, filter, PageRequest.of(i, 30,  Sort.by(orders)));
            List<NetDisk> list = page.getContent();
            list.forEach(netDisk -> System.out.println(netDisk.toString()));
            System.out.println();
        }
    }

    @Test
    public void select() {
        Iterable<NetDisk> all = repository.findAll();
        all.forEach(netDisk -> System.out.println(netDisk.toString()));
    }

    @Test
    public void deleteAll() {
        repository.deleteAll();
    }

    @Test
    public void addData() throws IOException {
        int id = 15681866;
        int batchSize = 30000;

        String filePathPrefix = "D:\\百度网盘目录\\";
        String[] files = {"唯----库.xlsx"};//15699808
        String jsonPrefix = "D:\\百度网盘目录\\json\\";

        for (String file : files) {
            try (InputStream is = new FileInputStream(filePathPrefix + file); ReadableWorkbook wb = new ReadableWorkbook(is)) {
                long sheets = wb.getSheets().count();
                for(long j=0;j<sheets; j++) {
                    int sheetIndex = 1;
                    Optional<Sheet> optional = wb.getSheet(Long.valueOf(j).intValue());
                    Sheet sheet = optional.get();

                    String jsonFilePath = jsonPrefix + file.replace(".xlsx","")+"_"+sheet.getName()+"_"+sheetIndex+".json";
                    File jsonFile = new File(jsonFilePath);
                    jsonFile.createNewFile();

                    List<Row> rows = sheet.read();
                    if (rows.size() == 0) {
                        continue;
                    }
                    for (int i = 2; i < rows.size(); i++) {
//                    for (int i = 2; i < 5; i++) {
                        Row row = rows.get(i);
                        String code = row.getCell(0) == null ? "" : row.getCell(0).getText();
                        String name = row.getCell(1) == null ? "" : row.getCell(1).getText();
                        String path = row.getCell(2) == null ? "" : row.getCell(2).getText();
                        String size = row.getCell(3) == null ? "" : row.getCell(3).getText();
                        String time = row.getCell(4) == null ? "" : row.getCell(4).getText();
                        String md5 = row.getCell(5) == null ? "" : row.getCell(5).getText();
                        JSONObject obj = new JSONObject();
                        obj.put("_index","netdisk-index"); obj.put("_type","_doc"); obj.put("_id",id);
                        JSONObject index = new JSONObject();
                        index.put("index", obj);
                        JSONObject netDisk = new JSONObject();
                        netDisk.put("code", code); netDisk.put("name", name); netDisk.put("path", path);
                        netDisk.put("size", size); netDisk.put("time", time); netDisk.put("md5", md5);

                        Files.write(Paths.get(jsonFilePath), (index.toString()+"\n").getBytes(), APPEND);
                        Files.write(Paths.get(jsonFilePath), (netDisk.toString()+"\n").getBytes(), APPEND);
                        if (i % batchSize == 0) {//可以设置不同的大小；如50，100，500，1000等等
                            sheetIndex++;
                            jsonFilePath = jsonPrefix + file.replace(".xlsx","")+"_"+sheet.getName()+"_"+sheetIndex+".json";
                            jsonFile = new File(jsonFilePath);
                            jsonFile.createNewFile();
                        }
                        System.out.println(file+"已写入"+ id+ "条数据");
                        id++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
