package com.fiora.note2.controller;

import com.fiora.note2.dao.NetDiskRepository;
import com.fiora.note2.model.NetDisk;
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

    @RequestMapping(value = "/netDisk", method = RequestMethod.GET)
    public ResponseEntity<List<String>> queryNetDisk(@RequestParam("filter") String filter, @RequestParam("token") String token) {
        if (!"fiora221".equals(token)) {
            return ResponseEntity.badRequest().build();
        }
        List<NetDisk> list = netDiskRepository.findByNameOrPath(filter);
        List<String> result = new ArrayList<>();
        for (NetDisk netDisk:list ) {
            result.add(netDisk.getPath()+netDisk.getName());
        }
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/string", method = RequestMethod.GET)
    public ResponseEntity<String> queryStringList(@RequestParam("filter") String filter, @RequestParam("token") String token) {
        if (!"fiora221".equals(token)) {
            return ResponseEntity.badRequest().build();
        }
        List<NetDisk> list = netDiskRepository.findByNameOrPath(filter);
        StringBuffer sb = new StringBuffer("");
        for (NetDisk netDisk:list ) {
            sb.append(netDisk.getPath()+netDisk.getName()+"=====");
        }
        return ResponseEntity.ok(sb.toString());
    }
}
