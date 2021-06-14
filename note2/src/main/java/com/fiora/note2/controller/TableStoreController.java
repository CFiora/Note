package com.fiora.note2.controller;

import com.alibaba.fastjson.JSON;
import com.fiora.note2.enums.StatusCode;
import com.fiora.note2.model.NetDisk;
import com.fiora.note2.model.Token;
import com.fiora.note2.service.TableStoreService;
import com.fiora.note2.service.TokenService;
import com.fiora.note2.util.Result;
import com.fiora.note2.vo.Page;
import com.fiora.note2.vo.TableStoreRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/ts")
public class TableStoreController {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private TableStoreService tableStoreService;
    public static final int pageNum = 1;
    public static final int pageSize = 10;

    @PostMapping
    public Result search(@RequestBody TableStoreRequest request) {
        Token token = tokenService.findPrivateToken();
        if(!token.getToken().equals(request.getToken())) {
            return Result.result(StatusCode.FAIL);
        }
        if(request.getPageNum() == null) {
            request.setPageNum(pageNum);
        }
        if(request.getPageSize() == null) {
            request.setPageSize(pageSize);
        }
        Page<NetDisk> page = tableStoreService.search(request.getMatch(), request.getFilter(), (request.getPageNum()-1) * request.getPageSize(), request.getPageSize());
        Object result = JSON.toJSON(page);
        return Result.ok(result);
    }
}
