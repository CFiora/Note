package com.fiora.note2.elastic;

import com.fiora.note2.dao.NetDiskRepository;
import com.fiora.note2.model.NetDisk;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.dhatim.fastexcel.reader.ReadableWorkbook;
import org.dhatim.fastexcel.reader.Row;
import org.dhatim.fastexcel.reader.Sheet;
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
@Slf4j
public class ESTest2 {

    @Autowired
    private NetDiskRepository repository;

    @Test
    public void addData2DB() throws IOException {
        int id = 0;
        int batchSize = 30000;

        String filePathPrefix = "D:\\百度网盘目录\\Excel\\";
        String[] files = {"防爆文件夹.xlsx"};
        String jsonPrefix = "D:\\百度网盘目录\\json\\";

        for (String file : files) {
            try (InputStream is = new FileInputStream(filePathPrefix + file); ReadableWorkbook wb = new ReadableWorkbook(is)) {
                long sheets = wb.getSheets().count();
                for(long j=0;j<sheets; j++) {
                    int sheetIndex = Long.valueOf(j).intValue()+1;
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
                        obj.put("_index","baidudisk-index"); obj.put("_type","_doc"); obj.put("_id",id);
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
                        log.info("{} 已写入 {} 条数据，目前文件是 {}", file, id, jsonFilePath);
                        id++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void createDirectory() {
        String filePathPrefix = "D:\\百度网盘目录\\Excel\\";
        String targetPathPrefix = "D:\\百度网盘目录\\目录\\yec498";
//        String[] files = {"2018更新资源0.xlsx", "2019更新资源0.xlsx", "2020更新资源0.xlsx", "KTV0.xlsx", "中美百万0.xlsx", "体育0.xlsx", "动漫0.xlsx", "图书0.xlsx", "教育0.xlsx", "更新资源0.xlsx", "游戏IT0.xlsx", "电影0.xlsx", "电视剧0.xlsx", "百家0.xlsx", "纪录片0.xlsx", "综艺0.xlsx", "蓝光专区0.xlsx", "音乐0.xlsx", "高清经典0.xlsx"};
//        String[] files = {"100T精品教程2.xlsx", "1500T补充.xlsx", "600T.xlsx", "鬼刀.xlsx"};
//        String[] files = {"防爆文件夹.xlsx"};
//        String[] files = {"G.【全网影音】.xlsx","知识学院课程.xlsx"};
//        String[] files = {"设计课堂.xlsx","IT课堂.xlsx","AAA云深资料库.xlsx", "各大平台.xlsx","免费课程分享群 2021.xlsx"};
//        String[] files = {"魑魅魍魉也爱书.xlsx"};
//        String[] files = {"斜杠青年研习社.xlsx"};
        String[] files = {"yec498-个人数据.xlsx"};
        List<String> error = new ArrayList<>();
        List<String> errorFiles = new ArrayList<>();
        for (String file : files) {
            try (InputStream is = new FileInputStream(filePathPrefix + file); ReadableWorkbook wb = new ReadableWorkbook(is)) {
                long sheets = wb.getSheets().count();
                for(long j=0;j<sheets; j++) {
                    Optional<Sheet> optional = wb.getSheet(Long.valueOf(j).intValue());
                    Sheet sheet = optional.get();
                    List<Row> rows = sheet.read();
                    if (rows.size() == 0) {
                        continue;
                    }
                    for (int i = 2; i < rows.size(); i++) {
//                    for (int i = 2; i < 5; i++) {
                        Row row = rows.get(i);
                        String path = row.getCell(2) == null ? "" : row.getCell(2).getText();
                        String size = row.getCell(3) == null ? "" : row.getCell(3).getText();
                        String targetPath = targetPathPrefix + path;
                        boolean b = isDirectory(size);
                        System.out.println(targetPath);
                        File toCreate = new File(targetPath);
                        if(b) {
                            try {
                                if(!toCreate.exists()) {
                                    boolean mkdirs = toCreate.mkdirs();
                                    if(!mkdirs) {
                                        error.add(path+"@"+file);
                                    }
                                }
                            } catch (Exception e) {
                                error.add(path+"@"+file);
                                e.printStackTrace();
                                log.error(path+"@"+file + "->" +e.getMessage());
                            }
                        } else {
                            try {
                                File parentDir = toCreate.getParentFile();
                                if (parentDir.isDirectory()) {
                                    if(!parentDir.exists()) {
                                        parentDir.mkdirs();
                                    }
                                    if(!toCreate.exists()) {
                                        boolean newFile = toCreate.createNewFile();
                                        if(!newFile) {
                                            errorFiles.add(path+"@"+file);
                                        }
                                    }
                                } else {
                                    parentDir = new File(parentDir.getAbsolutePath()+"_dir");
                                    if(!parentDir.exists()) {
                                        parentDir.mkdirs();
                                    }
                                    toCreate = new File(parentDir.getAbsolutePath() + "/" + toCreate.getName());
                                    if(!toCreate.exists()) {
                                        boolean newFile = toCreate.createNewFile();
                                        if(!newFile) {
                                            errorFiles.add(path+"@"+file);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                errorFiles.add(path+"@"+file);
                                e.printStackTrace();
                                log.error(path+"@"+file + "->" +e.getMessage());
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("error -> " + error.toString());
        log.info("error -> " + error.toString());
        System.out.println("error Files -> " + errorFiles.toString());
        log.info("error Files -> " + errorFiles.toString());
    }

    private static boolean isDirectory(String path) {
//        int dotIndex = path.lastIndexOf(".");
//        if (dotIndex != -1 && dotIndex != path.length() -1) {
//            String suffix = path.substring(dotIndex + 1);
//            if (suffix.length() <= 5 && suffix.replaceAll("[A-Za-z0-9]", "").length() == 0) {
//                return false;
//            }
//        }
//        return true;

        return "0.00kb".equals(path);
    }

    public static void main(String[] args) throws IOException {
        test1();
    }

    public static void test1() throws IOException {
        String targetPathPrefix = "D:\\百度网盘目录\\目录";
        String path = "/0防爆文件夹/ebook/1号网盘/汇总资源等多个文件/A卷/39/霸道总裁系列小说150篇/总裁1—50本.rar/沈盈 总裁的金援新娘.txt";
        String targetPath = targetPathPrefix + path;
        boolean b = isDirectory(targetPath);
        File toCreate = new File(targetPath);
        File parentDir = toCreate.getParentFile();
        if (parentDir.isDirectory()) {
            if(!parentDir.exists()) {
                parentDir.mkdirs();
            }
            if(!toCreate.exists()) {
                boolean newFile = toCreate.createNewFile();
                System.out.println("newFile -> " + newFile);
            }
        } else {
            parentDir = new File(parentDir.getAbsolutePath()+"_dir");
            if(!parentDir.exists()) {
                boolean mkdirs = parentDir.mkdirs();
                System.out.println("mkdirs -> " + mkdirs);
            }
            System.out.println(toCreate.getName());
            File temp = new File(parentDir.getAbsolutePath() + "/" + toCreate.getName());
            System.out.println(temp.getAbsolutePath());
            if(!temp.exists()) {
                boolean newFile = temp.createNewFile();
                System.out.println("newFile -> " + newFile);
            }
        }
    }

    @Test
    public void addData_bak() throws IOException {
        int id = 2142460;
        int batchSize = 30000;

        String filePathPrefix = "D:\\百度网盘目录\\";
        String[] files = {"防爆文件夹.xlsx"};//2142459
        String jsonPrefix = "D:\\百度网盘目录\\json\\";

        for (String file : files) {
            try (InputStream is = new FileInputStream(filePathPrefix + file); ReadableWorkbook wb = new ReadableWorkbook(is)) {
                long sheets = wb.getSheets().count();
                for(long j=0;j<sheets; j++) {
                    int sheetIndex = Long.valueOf(j).intValue()+1;
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
                        obj.put("_index","baidudisk-index"); obj.put("_type","_doc"); obj.put("_id",id);
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
                        log.info("{} 已写入 {} 条数据，目前文件是 {}", file, id, jsonFilePath);
                        id++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //    @Test
//    public void pageTest() {
//        String filter = "公务员";
//        List<Sort.Order> orders = new ArrayList();
//        orders.add(new Sort.Order(Sort.Direction.DESC,"_score"));
//        for (int i = 0; i < 4; i++) {
//            Page<NetDisk> page = repository.findByNameLikeOrPathLike(filter, filter, PageRequest.of(i, 30,  Sort.by(orders)));
//            List<NetDisk> list = page.getContent();
//            list.forEach(netDisk -> System.out.println(netDisk.toString()));
//            System.out.println();
//        }
//    }
//
//    @Test
//    public void select() {
//        Iterable<NetDisk> all = repository.findAll();
//        all.forEach(netDisk -> System.out.println(netDisk.toString()));
//    }
//
//    @Test
//    public void deleteAll() {
//        repository.deleteAll();
//    }
}
