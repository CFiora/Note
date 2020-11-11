package com.fiora.note2.controller;

import com.fiora.note2.dao.NetDiskDao;
import com.fiora.note2.model.NetDisk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class APIController {

    @Autowired
    private NetDiskDao netDiskDao;

    public List<NetDisk> queryNetDisk() {
        return netDiskDao.findByNameOrPath("");
    }
}
