package com.fiora.note2.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name="net_disk_1")
@Data
@NoArgsConstructor
public class NetDisk1 {
    public NetDisk1(String name, String path) {
        this.name = name;
        this.path = path;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
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