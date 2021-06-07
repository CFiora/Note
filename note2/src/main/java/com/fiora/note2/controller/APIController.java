//package com.fiora.note2.controller;
//
//import com.fiora.note2.dao.NetDisk1Repository;
//import com.fiora.note2.dao.NetDiskRepository;
//import com.fiora.note2.model.NetDisk;
//import com.fiora.note2.model.NetDisk1;
//import com.fiora.note2.util.ExcelUtil;
//import lombok.extern.slf4j.Slf4j;
//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
//import org.apache.poi.ss.usermodel.Row;
//import org.apache.poi.ss.usermodel.Sheet;
//import org.apache.poi.ss.usermodel.Workbook;
//import org.apache.shiro.authz.annotation.RequiresPermissions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.*;
//
//@RestController
//@RequestMapping("api")
//@Slf4j
//public class APIController {
//
//    @Autowired
//    private NetDiskRepository netDiskRepository;
//    @Autowired
//    private NetDisk1Repository netDiskRepository1;
//
//    @RequiresPermissions("api:select")
//    @RequestMapping(value = "/netDisk", method = RequestMethod.POST)
//    public ResponseEntity<List<NetDisk>> queryNetDisk(@RequestParam("filter") String filter) {
//        return ResponseEntity.ok(netDiskRepository.findByNameOrPath(filter));
//    }
//
//    @RequiresPermissions("api:select")
//    @RequestMapping(value = "/list", method = RequestMethod.GET)
//    public ResponseEntity<List<String>> queryStringList(@RequestParam("filter") String filter) {
//        List<NetDisk> list = netDiskRepository.findByNameOrPath(filter);
//        List<String> result = new ArrayList<>();
//        StringBuffer sb = new StringBuffer("");
//        for (NetDisk netDisk:list ) {
//            result.add(netDisk.getPath()+netDisk.getName());
//        }
//        return ResponseEntity.ok(result);
//    }
//
//    @RequiresPermissions("api:select")
//    @RequestMapping(value = "/string", method = RequestMethod.GET)
//    public ResponseEntity<String> queryString(@RequestParam("filter") String filter, String token) {
//        if (!token.equals("fiora221")) {
//            return ResponseEntity.badRequest().build();
//        }
//        List<NetDisk> list = netDiskRepository.findByNameOrPath(filter);
//        JSONArray jsonArray = new JSONArray();
//        for (NetDisk netDisk:list ) {
//            jsonArray.add(netDisk.getPath()+netDisk.getName());
//        }
//        return ResponseEntity.ok(jsonArray.toString());
//    }
//
//    @RequestMapping(value = "/test", method = RequestMethod.GET)
//    public ResponseEntity<List<NetDisk>> test() {
//        List<NetDisk> list = new ArrayList<>();
//        NetDisk n1 = new NetDisk("name1","path1");
//        NetDisk n2 = new NetDisk("name2","path2");
//        NetDisk n3 = new NetDisk("name3","path3");
//        list.add(n1);list.add(n2);list.add(n3);
//        return ResponseEntity.ok(list);
//    }
//
//    @RequiresPermissions("api:select")
//    @RequestMapping(value = "/netDisk2", method = RequestMethod.POST)
//    public void queryNetDisk2(@RequestParam("filter") String filter,
//               HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        List<NetDisk> result = netDiskRepository.findByNameOrPath(filter);
//        request.setAttribute("data",result);
//        request.getRequestDispatcher("/netDiskSearch.jsp").forward(request,response);
//    }
//}
