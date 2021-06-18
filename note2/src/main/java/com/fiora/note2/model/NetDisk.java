package com.fiora.note2.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "netdisk-index")
public class NetDisk implements Serializable {
    public NetDisk(String name, String path) {
        this.name = name;
        this.path = path;
    }
    @Id
    private int id;
    private String code;
    private String name;
    private String path;
    private String size;
    private String time;
    private String md5;
    private String directory;
}
