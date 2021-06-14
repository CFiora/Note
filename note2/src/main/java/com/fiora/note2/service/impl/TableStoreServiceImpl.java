package com.fiora.note2.service.impl;

import com.alicloud.openservices.tablestore.SyncClient;
import com.alicloud.openservices.tablestore.model.Row;
import com.alicloud.openservices.tablestore.model.search.SearchQuery;
import com.alicloud.openservices.tablestore.model.search.SearchRequest;
import com.alicloud.openservices.tablestore.model.search.SearchResponse;
import com.alicloud.openservices.tablestore.model.search.query.BoolQuery;
import com.alicloud.openservices.tablestore.model.search.query.MatchQuery;
import com.alicloud.openservices.tablestore.model.search.sort.ScoreSort;
import com.alicloud.openservices.tablestore.model.search.sort.Sort;
import com.fiora.note2.model.NetDisk;
import com.fiora.note2.service.TableStoreService;
import com.fiora.note2.vo.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TableStoreServiceImpl implements TableStoreService {
    @Autowired
    private SyncClient syncClient;
    @Autowired
    private TableStoreService tableStoreService;
    private static String tableName = "net_disk";
    private static String tableIndex = "net_disk_index";
    private static String otsPk = "id_md5";

    @Override
    public Page<NetDisk> search(String match, String filter, int offSet, int limit) {
        Page<NetDisk> page = new Page<>();

        MatchQuery nameMatchQuery = new MatchQuery();
        nameMatchQuery.setFieldName("name");
        nameMatchQuery.setText(match);

        MatchQuery pathMatchQuery = new MatchQuery();
        pathMatchQuery.setFieldName("path");
        pathMatchQuery.setText(match);

        SearchQuery searchQuery = new SearchQuery();
        BoolQuery boolQuery = new BoolQuery();
        boolQuery.setShouldQueries(Arrays.asList(nameMatchQuery, pathMatchQuery));
        boolQuery.setMinimumShouldMatch(1); //设置至少满足一个条件。
        if (!ObjectUtils.isEmpty(filter)) {
            MatchQuery nameMatchQuery2 = new MatchQuery();
            nameMatchQuery2.setFieldName("name");
            nameMatchQuery2.setText(filter);

            MatchQuery pathMatchQuery2 = new MatchQuery();
            pathMatchQuery2.setFieldName("path");
            pathMatchQuery2.setText(filter);

            boolQuery.setMustNotQueries(Arrays.asList(nameMatchQuery2, pathMatchQuery2));
        }

        searchQuery.setQuery(boolQuery);
        searchQuery.setGetTotalCount(true);
        searchQuery.setOffset(offSet);
        searchQuery.setLimit(limit);
        searchQuery.setSort(new Sort(Arrays.asList(new ScoreSort())));

        SearchRequest searchRequest = new SearchRequest(tableName, tableIndex, searchQuery);
        SearchRequest.ColumnsToGet columnsToGet = new SearchRequest.ColumnsToGet();
        columnsToGet.setReturnAll(true); //设置为返回所有列。
        searchRequest.setColumnsToGet(columnsToGet);
        SearchResponse resp = syncClient.search(searchRequest);
        page.setTotalCount(resp.getTotalCount());
        List<NetDisk> list = new ArrayList<>();
        for(Row row:resp.getRows()) {
            NetDisk netDisk = new NetDisk();
            netDisk.setName(row.getColumn("name").get(0).getValue().asString());
            netDisk.setPath(row.getColumn("path").get(0).getValue().asString());
            netDisk.setSize(row.getColumn("size").get(0).getValue().asString());
            netDisk.setId(row.getPrimaryKey().getPrimaryKeyColumn("id_md5").getValue().asString());
            list.add(netDisk);
        }
        page.setResult(list);
        return page;
    }
}
