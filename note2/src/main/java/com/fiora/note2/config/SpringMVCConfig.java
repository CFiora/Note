package com.fiora.note2.config;

import com.alicloud.openservices.tablestore.SyncClient;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiora.note2.model.Token;
import com.fiora.note2.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


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

    @Bean
    @SuppressWarnings("all")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(jackson2JsonRedisSerializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }
}
