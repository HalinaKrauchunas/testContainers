package com.example.testconatainersdemo.controller;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.example.testconatainersdemo.entity.Dev;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class DynamoDbController {
    private DynamoDBMapper dynamoDBMapper;
    private DynamoDBMapperConfig dynamoDBMapperConfig;

    @Autowired
    public DynamoDbController(
        DynamoDBMapper dynamoDBMapper,
        DynamoDBMapperConfig dynamoDBMapperConfig
    ) {
        this.dynamoDBMapper = dynamoDBMapper;
        this.dynamoDBMapperConfig = dynamoDBMapperConfig;
    }

    @GetMapping(value = "/dynamo/{devId}")
    public Dev getDevByIdPlusHalina(@PathVariable(value = "devId") String devId) {
        Dev dev = dynamoDBMapper.load(Dev.class, devId, dynamoDBMapperConfig);
        dev.setDevId(dev.getDevId() + "_HALINA");
        return dev;
    }
}
