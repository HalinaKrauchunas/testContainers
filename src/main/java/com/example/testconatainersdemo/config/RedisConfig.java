package com.example.testconatainersdemo.config;


import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisConfig {
    @Value("${redis.host}")
    private String host;

    @Value("${redis.port}")
    private String port;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.setCodec(JsonJacksonCodec.INSTANCE);
        config.useSingleServer().setAddress("redis://" + host + ":" + port);
        return Redisson.create(config);
    }
}
