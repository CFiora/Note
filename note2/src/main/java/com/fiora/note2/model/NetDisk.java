package com.fiora.note2.model;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="net_disk")
@Data
@NoArgsConstructor
public class NetDisk {
    public NetDisk(String name, String path) {
        this.name = name;
        this.path = path;
    }
    @Id
    @Column(name = "code")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "path")
    private String path;
    @Column(name = "size")
    private String size;
    @Column(name = "time")
    private String time;
    @Column(name = "md5")
    private String md5;
    @Column(name = "directory")
    private String directory;
}
