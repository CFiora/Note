package com.fiora.note2.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "net_disk")
//@Document(indexName = "netdisk-index")
public class NetDisk implements Serializable {
    public NetDisk(String name, String path) {
        this.name = name;
        this.path = path;
    }
    @Id
    @GeneratedValue( strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String path;
//    private String code;
//    private String size;
//    private String time;
//    private String md5;
//    private String directory;
}
