package com.fiora.note2.controller;

import com.fiora.note2.dao.NetDiskRepository;
import com.fiora.note2.model.NetDisk;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api")
public class APIController {

    @Autowired
    private NetDiskRepository netDiskRepository;

    @RequiresPermissions("api:select")
    @RequestMapping(value = "/netDisk", method = RequestMethod.POST)
    public ResponseEntity<List<NetDisk>> queryNetDisk(@RequestParam("filter") String filter) {
        return ResponseEntity.ok(netDiskRepository.findByNameOrPath(filter));
    }

    @RequiresPermissions("api:select")
    @RequestMapping(value = "/string", method = RequestMethod.POST)
    public ResponseEntity<List<String>> queryStringList(@RequestParam("filter") String filter) {
        List<NetDisk> list = netDiskRepository.findByNameOrPath(filter);
        List<String> result = new ArrayList<>();
        StringBuffer sb = new StringBuffer("");
        for (NetDisk netDisk:list ) {
            result.add(netDisk.getPath()+netDisk.getName()+"=====");
        }
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public ResponseEntity<List<NetDisk>> test() {
        List<NetDisk> list = new ArrayList<>();
        NetDisk n1 = new NetDisk("name1","path1");
        NetDisk n2 = new NetDisk("name2","path2");
        NetDisk n3 = new NetDisk("name3","path3");
        list.add(n1);list.add(n2);list.add(n3);
        return ResponseEntity.ok(list);
    }
}
