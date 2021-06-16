//package com.fiora.note2.elastic;
//
//import com.fiora.note2.model.NetDisk;
//import io.searchbox.client.JestClient;
//import io.searchbox.core.Index;
//import io.searchbox.core.Search;
//import io.searchbox.core.SearchResult;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.io.IOException;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class ElasticSearchTest {
//    @Autowired
//    private JestClient jestClient;
//
//
//    @Test
//    public void contextLoad() throws IOException {
//        NetDisk netDisk = new NetDisk().setId(1).setCode("code")
//                .setDirectory("directory").setMd5("md5").setName("name").setPath("path").setSize("size").setTime("time");
//        Index index = new Index.Builder(netDisk).index("index").type("test").build();
//        jestClient.execute(index);
//    }
//
//    @Test
//    public void search() throws IOException {
//        String json = "{\n" +
//                "    \"query\" : {\n" +
//                "        \"match\" : {\n" +
//                "            \"name\" : \"name\"\n" +
//                "        }\n" +
//                "    }\n" +
//                "}";
//        Search search = new Search.Builder(json).addIndex("index").addType("test").build();
//        SearchResult result = jestClient.execute(search);
//        System.out.println(result.getJsonString());
//    }
//
//}
