package com.fiora.note2.model;
import lombok.Data;

import javax.persistence.Entity;

@Entity
@Table(name="net_disk")
@Data
public class NetDisk {
    private String code;
    private String name;
    private String path;
    private String size;
    private String time;
    private String md5;
    private String directory;
}
