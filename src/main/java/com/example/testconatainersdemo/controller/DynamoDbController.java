package com.example.testconatainersdemo.controller;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.example.testconatainersdemo.entity.Dev;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RList;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class DynamoDbController {
    private RedissonClient redissonClient;
    private DynamoDBMapper dynamoDBMapper;
    private DynamoDBMapperConfig dynamoDBMapperConfig;

    @Autowired
    public DynamoDbController(
            DynamoDBMapper dynamoDBMapper,
            DynamoDBMapperConfig dynamoDBMapperConfig,
            RedissonClient redissonClient
    ) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.dynamoDBMapperConfig = dynamoDBMapperConfig;
        this.redissonClient = redissonClient;
    }

    @GetMapping(value = "/dynamo/{devId}")
    public Dev getDevById(@PathVariable(value = "devId") String devId) {
        RMap<String, Dev> rMap = redissonClient.getMap("MyMap");
//        get Dev from rMap by devId
        Dev dev = rMap.get(devId);
//        if dev is null get Dev from Dynamo and storage in redis
        if (Objects.isNull(dev)) {
            dev = dynamoDBMapper.load(Dev.class, devId, dynamoDBMapperConfig);
            rMap.put(devId, dev);
            dev.setDevId(dev.getDevId() + "_HALINA");
        }
        return dev;
    }
}
