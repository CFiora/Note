package com.fiora.note2.config;

import com.fiora.note2.service.TokenService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Configuration
public class TableStoreConfig {
    @Value("https://note-2.cn-hangzhou.ots.aliyuncs.com")
    private String endPoint;

    private String accessKeyId;

    private String accessKeySecret;

    @Value("note-2")
    private String instanceName;

}
