package com.fiora.note2.config;

import com.alicloud.openservices.tablestore.SyncClient;
import com.fiora.note2.model.Token;
import com.fiora.note2.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
public class SpringMVCConfig {
    @Autowired
    private TokenService tokenService;

    @Bean
    public TableStoreConfig getTableStoreConfig() {
        TableStoreConfig tsConfig = new TableStoreConfig();
        Token aliyunToken = tokenService.findAliyunToken();
        String[] arr = aliyunToken.getToken().split("@@");
        tsConfig.setAccessKeyId(arr[0]);
        tsConfig.setAccessKeySecret(arr[1]);
        return  tsConfig;
    }

    @Bean
    public SyncClient syncClient() {
        TableStoreConfig tsConfig = getTableStoreConfig();
        log.info("tableStoreConfig -> " + tsConfig);
        return new SyncClient(tsConfig.getEndPoint(), tsConfig.getAccessKeyId(), tsConfig.getAccessKeySecret(), tsConfig.getInstanceName());
    }

}
