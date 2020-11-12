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
    public ResponseEntity<List<NetDisk>> queryNetDisk(@RequestParam("filter") String filter, @RequestParam("token") String token) {
        if (!"fiora221".equals(token)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(netDiskRepository.findByNameOrPath("蜀绣"));
    }

    @RequestMapping(value = "/string", method = RequestMethod.GET)
    public ResponseEntity<List<String>> queryStringList(@RequestParam("filter") String filter, @RequestParam("token") String token) {
        if (!"fiora221".equals(token)) {
            return ResponseEntity.badRequest().build();
        }
        List<String> list = new ArrayList<>();
        list.add("111");
        list.add("222");
        return ResponseEntity.ok(list);
    }
}
