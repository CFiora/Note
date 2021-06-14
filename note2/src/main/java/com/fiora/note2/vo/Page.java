package com.fiora.note2.vo;

import lombok.Data;

import java.util.List;

@Data
public class Page<T> {
    long totalCount;
    List<T> result;
}
