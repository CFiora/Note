package com.fiora.note2.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TableStoreRequest implements Serializable {
    private String match;
    private String filter;
    private Integer pageNum;
    private Integer pageSize;
    private String token;
}
