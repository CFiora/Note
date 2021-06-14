package com.fiora.note2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NetDisk {
    public NetDisk(String name, String path) {
        this.name = name;
        this.path = path;
    }
    private String id;
    private String code;
    private String name;
    private String path;
    private String size;
    private String time;
    private String md5;
    private String directory;
}
