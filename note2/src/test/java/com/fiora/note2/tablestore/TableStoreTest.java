//package com.fiora.note2.tablestore;
//
//import com.alibaba.fastjson.JSON;
//import com.alicloud.openservices.tablestore.SyncClient;
//import com.alicloud.openservices.tablestore.model.*;
//import com.alicloud.openservices.tablestore.model.search.*;
//import com.alicloud.openservices.tablestore.model.search.analysis.SingleWordAnalyzerParameter;
//import com.alicloud.openservices.tablestore.model.search.query.BoolQuery;
//import com.alicloud.openservices.tablestore.model.search.query.MatchQuery;
//import com.alicloud.openservices.tablestore.model.search.sort.ScoreSort;
//import com.alicloud.openservices.tablestore.model.search.sort.Sort;
//import com.fiora.note2.util.IDUtil;
//import lombok.extern.slf4j.Slf4j;
//import org.dhatim.fastexcel.reader.ReadableWorkbook;
//import org.dhatim.fastexcel.reader.Row;
//import org.dhatim.fastexcel.reader.Sheet;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.util.ObjectUtils;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.*;
//
//@SpringBootTest
//@Slf4j
//public class TableStoreTest {
//    @Autowired
//    private SyncClient syncClient;
//    private static String tableName = "net_disk";
//    private static String tableIndex = "net_disk_index";
//    private static String otsPk = "id_md5";
//
//    @Test
//    public void createTable() {
//        log.info("end -> " + syncClient.getEndpoint());
//        log.info("instance -> " + syncClient.getInstanceName());
//        ListTableResponse listTableResponse = syncClient.listTable();
//        log.info("table list: " + listTableResponse.getTableNames());
//        if(listTableResponse.getTableNames().contains(tableName)) {
//            return ;
//        }
//        TableMeta tableMeta = new TableMeta(tableName);
//        //获取主键
//        tableMeta.addPrimaryKeyColumn(new PrimaryKeySchema(otsPk, PrimaryKeyType.STRING)); // 为主表添加主键列。
//        int timeToLive = -1; // 数据的过期时间，单位秒, -1代表永不过期，例如设置过期时间为一年, 即为 365 * 24 * 3600。
//        int maxVersions = 1; // 保存的最大版本数，设置为3即代表每列上最多保存3个最新的版本。
//        TableOptions tableOptions = new TableOptions(timeToLive, maxVersions);
//        CreateTableRequest createTableRequest = new CreateTableRequest(tableMeta, tableOptions);
//        createTableRequest.setReservedThroughput(new ReservedThroughput(new CapacityUnit(0, 0))); // 设置读写预留值，容量型实例只能设置为0，高性能实例可以设置为非零值。
//        syncClient.createTable(createTableRequest);
//
//        CreateSearchIndexRequest indexRequest = new CreateSearchIndexRequest();
//        indexRequest.setTableName(tableName); //设置数据表名称。
//        indexRequest.setIndexName(tableIndex); //设置多元索引名称。
//        IndexSchema indexSchema = new IndexSchema();
//        indexSchema.setFieldSchemas(Arrays.asList(
//                new FieldSchema("name", FieldType.TEXT) //设置字段名称和类型。
//                        .setIndex(true).setAnalyzer(FieldSchema.Analyzer.SingleWord)
//                        .setAnalyzerParameter(new SingleWordAnalyzerParameter(false, true)),
//                new FieldSchema("path", FieldType.TEXT) //设置字段名称和类型。
//                        .setIndex(true).setAnalyzer(FieldSchema.Analyzer.SingleWord)
//                        .setAnalyzerParameter(new SingleWordAnalyzerParameter(false, true))
//        ));
//
//        indexRequest.setIndexSchema(indexSchema);
//        syncClient.createSearchIndex(indexRequest);
//
//        ListTableResponse listTableResponse2 = syncClient.listTable();
//        log.info("table list: " + listTableResponse2.getTableNames());
//
//    }
//
//    @Test
//    public void truncateIndex() {
//        ListSearchIndexRequest request = new ListSearchIndexRequest();
//        request.setTableName(tableName); //设置数据表名称。
//        log.info(syncClient.listSearchIndex(request).getIndexInfos().toString()); //获取数据表关联的所有多元索引。
//
//        DeleteSearchIndexRequest delRequest = new DeleteSearchIndexRequest();
//        delRequest.setTableName(tableName); //设置数据表名称。
//        delRequest.setIndexName(tableIndex); //设置多元索引名称。
//        syncClient.deleteSearchIndex(delRequest); //调用client删除多元索引。
//
//        request = new ListSearchIndexRequest();
//        request.setTableName(tableName); //设置数据表名称。
//        log.info(syncClient.listSearchIndex(request).getIndexInfos().toString()); //获取数据表关联的所有多元索引。
//    }
//
//    @Test
//    public void truncateTable() {
//        ListTableResponse listTableResponse = syncClient.listTable();
//        log.info("table list: " + listTableResponse.getTableNames());
//
//        DeleteSearchIndexRequest request = new DeleteSearchIndexRequest();
//        request.setTableName(tableName); //设置数据表名称。
//        request.setIndexName(tableIndex); //设置多元索引名称。
//        syncClient.deleteSearchIndex(request); //调用client删除多元索引。
//
//        // 删除表
//        DeleteTableRequest deleteTableRequest = new DeleteTableRequest(tableName);
//        syncClient.deleteTable(deleteTableRequest);
//
//        ListTableResponse listTableResponse2 = syncClient.listTable();
//        log.info("table list: " + listTableResponse2.getTableNames());
//    }
//
//    @Test
//    public void searchTable() {
//        log.info("end -> " + syncClient.getEndpoint());
//        log.info("instance -> " + syncClient.getInstanceName());
//        ListTableResponse listTableResponse = syncClient.listTable();
//        log.info("table list: " + listTableResponse.getTableNames());
//        String match = "Vue";
//        String filter = "/IT";
//
//        MatchQuery nameMatchQuery = new MatchQuery();
//        nameMatchQuery.setFieldName("name");
//        nameMatchQuery.setText(match);
//
//        MatchQuery pathMatchQuery = new MatchQuery();
//        pathMatchQuery.setFieldName("path");
//        pathMatchQuery.setText(match);
//
//        SearchQuery searchQuery = new SearchQuery();
//        BoolQuery boolQuery = new BoolQuery();
//        boolQuery.setShouldQueries(Arrays.asList(nameMatchQuery, pathMatchQuery));
//        boolQuery.setMinimumShouldMatch(1); //设置至少满足一个条件。
//        if (!ObjectUtils.isEmpty(filter)) {
//            MatchQuery nameMatchQuery2 = new MatchQuery();
//            nameMatchQuery2.setFieldName("name");
//            nameMatchQuery2.setText(filter);
//
//            MatchQuery pathMatchQuery2 = new MatchQuery();
//            pathMatchQuery2.setFieldName("path");
//            pathMatchQuery2.setText(filter);
//
//            boolQuery.setMustNotQueries(Arrays.asList(nameMatchQuery2, pathMatchQuery2));
//        }
//
//        searchQuery.setQuery(boolQuery);
//        searchQuery.setGetTotalCount(true);
//        searchQuery.setOffset(0);
//        searchQuery.setLimit(10);
//        searchQuery.setSort(new Sort(Arrays.asList(new ScoreSort())));
//
//        SearchRequest searchRequest = new SearchRequest(tableName, tableIndex, searchQuery);
//        SearchRequest.ColumnsToGet columnsToGet = new SearchRequest.ColumnsToGet();
//        columnsToGet.setReturnAll(true); //设置为返回所有列。
//        searchRequest.setColumnsToGet(columnsToGet);
//
//
//
//        SearchResponse resp = syncClient.search(searchRequest);
//        log.info("TotalCount: " + resp.getTotalCount()); //打印匹配到的总行数，非返回行数
//        log.info("Row: " + resp.getRows());
//    }
//
//    @Test
//    public void putToOts() throws IOException {
//        String filePathPrefix = "D:\\百度网盘目录\\";
//        String[] files = {"G.【全网影音】.xlsx",// 2238444
//                "个人数据目录.xlsx","唯----库.xlsx","小九-图书.xlsx","小九-知识付费.xlsx","斜杠青年研习社.xlsx","知识学院课程.xlsx","防爆文件夹.xlsx",
//                "1500T\\2018更新资源0.xlsx","1500T\\2019更新资源0.xlsx","1500T\\2020更新资源0.xlsx","1500T\\KTV0.xlsx","1500T\\中美百万0.xlsx",
//                "1500T\\体育0.xlsx","1500T\\动漫0.xlsx","1500T\\图书0.xlsx","1500T\\教育0.xlsx","1500T\\更新资源0.xlsx","1500T\\游戏IT0.xlsx",
//                "1500T\\电影0.xlsx","1500T\\电视剧0.xlsx","1500T\\百家0.xlsx","1500T\\纪录片0.xlsx","1500T\\综艺0.xlsx","1500T\\蓝光专区0.xlsx",
//                "1500T\\音乐0.xlsx","1500T\\高清经典0.xlsx",
//                "1500T外\\100T精品教程2.xlsx", "1500T外\\1500T补充.xlsx", "1500T外\\600T.xlsx", "1500T外\\鬼刀.xlsx"
//        };
//        int total = 0;
//        int starup = 6910284;
//        int batchSize = 200;
//
//        for (String file : files) {
//            try (InputStream is = new FileInputStream(filePathPrefix + file); ReadableWorkbook wb = new ReadableWorkbook(is)) {
//                long sheets = wb.getSheets().count();
//                for(long j=0;j<sheets; j++) {
//                    Optional<Sheet> optional = wb.getSheet(Long.valueOf(j).intValue());
//                    Sheet sheet = optional.get();
//
//                    List<Row> rows = sheet.read();
//                    if (rows.size() == 0) {
//                        continue;
//                    }
//                    int subTotal = 0;
//                    BatchWriteRowRequest batchWriteRowRequest = new BatchWriteRowRequest();
//                    for (int i = 0; i < rows.size(); i++) {
//                        if ((sheet.getName().equals("第0部分") || sheet.getName().equals("Sheet")) && i < 2) {
//                            continue;
//                        }
//                        Row row = rows.get(i);
//                        try {
//                            String directory = row.getCell(6) == null ? "" : row.getCell(6).getText();
//                            if ("1".equals(directory) || "".equals(directory)) {
//                                log.info(String.format("continue directory total -> %d %s %s 已写入 %d 条数据", total, file, sheet.getName(), subTotal));
//                                continue;
//                            }
//                        } catch (Exception e) {
//                            log.error(String.format("continue exception total -> %d %s %s 已写入 %d 条数据", total, file, sheet.getName(), subTotal));
//                        }
//                        total++;subTotal++;
//                        if (total <= starup) {
//                            log.info(String.format("continue total -> %d %s %s 已写入 %d 条数据", total, file, sheet.getName(), subTotal));
//                            continue;
//                        }
//
//                        Map<String, String> map = new HashMap<>();
//                        getStringStringMap(row, map);
//
//                        PrimaryKeyBuilder primaryKeyBuilder = PrimaryKeyBuilder.createPrimaryKeyBuilder();
//                        primaryKeyBuilder.addPrimaryKeyColumn(otsPk, PrimaryKeyValue.fromString(IDUtil.getNetDiskId()));
//                        PrimaryKey primaryKey = primaryKeyBuilder.build();
//                        RowPutChange rowPutChange = new RowPutChange(tableName, primaryKey);
//
//                        assignRowChange(map, rowPutChange);
//                        batchWriteRowRequest.addRowChange(rowPutChange);
//
//                        if (subTotal % batchSize == 0) {//可以设置不同的大小；如50，100，500，1000等等
//                            log.info(String.format("total -> %d %s %s 已写入 %d 条数据", total, file, sheet.getName(), subTotal));
//                            sendBatchWriteRowRequest(batchWriteRowRequest);
//                            batchWriteRowRequest  = new BatchWriteRowRequest();
//                        }
//                    }
//                    if (subTotal % batchSize != 0 && total > starup) {
//                        log.info(String.format("total -> %d %s %s 已写入 %d 条数据", total, file, sheet.getName(), subTotal));
//                        sendBatchWriteRowRequest(batchWriteRowRequest);
//                    }
//                }
//            }
//        }
//    }
//
//    private void assignRowChange(Map<String, String> map, RowPutChange rowPutChange) {
//        for (String key : map.keySet()) {
//            Object value = map.get(key) == null ? "" : map.get(key);
//            if (value instanceof Long || value instanceof Integer) {
//                rowPutChange.addColumn(new Column(key, ColumnValue.fromLong(Long.valueOf(String.valueOf(value)))));
//            } else if (value instanceof Double || value instanceof Float) {
//                rowPutChange.addColumn(new Column(key, ColumnValue.fromDouble(Double.valueOf(String.valueOf(value)))));
//            } else if (value instanceof Boolean) {
//                rowPutChange.addColumn(new Column(key, ColumnValue.fromBoolean((Boolean) value)));
//            } else if (value instanceof List) {
//                rowPutChange.addColumn(new Column(key, ColumnValue.fromString(JSON.toJSONString(value))));
//            } else {
//                rowPutChange.addColumn(new Column(key, ColumnValue.fromString(String.valueOf(value))));
//            }
//        }
//    }
//
//
//    private void getStringStringMap(Row row, Map<String, String> map) {
//        String code = row.getCell(0) == null ? "" : row.getCell(0).getText();
//        String name = row.getCell(1) == null ? "" : row.getCell(1).getText();
//        String path = row.getCell(2) == null ? "" : row.getCell(2).getText();
//        String size = row.getCell(3) == null ? "" : row.getCell(3).getText();
//        String time = row.getCell(4) == null ? "" : row.getCell(4).getText();
//        String md5 = row.getCell(5) == null ? "" : row.getCell(5).getText();
//        map.put("name", name);
//        map.put("path", path);
//        map.put("size", size);
//    }
//
//
//    private void sendBatchWriteRowRequest(BatchWriteRowRequest batchWriteRowRequest) {
//        BatchWriteRowResponse response = syncClient.batchWriteRow(batchWriteRowRequest);
//        log.info("是否全部成功：" + response.isAllSucceed());
//        if (!response.isAllSucceed()) {
//            for (BatchWriteRowResponse.RowResult rowResult : response.getFailedRows()) {
//                log.error("失败的行：" + batchWriteRowRequest.getRowChange(rowResult.getTableName(), rowResult.getIndex()).getPrimaryKey());
//                log.error("失败原因：" + rowResult.getError());
//            }
//            /**
//             * 可以通过createRequestForRetry方法再构造一个请求对失败的行进行重试。此处只给出构造重试请求的部分。
//             * 推荐的重试方法是使用SDK的自定义重试策略功能，支持对batch操作的部分行错误进行重试。设置重试策略后，调用接口处无需增加重试代码。
//             */
//            BatchWriteRowRequest retryRequest = batchWriteRowRequest.createRequestForRetry(response.getFailedRows());
//        }
//    }
//}
