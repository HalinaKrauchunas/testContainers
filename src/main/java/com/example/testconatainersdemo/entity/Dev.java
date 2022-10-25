package com.example.testconatainersdemo.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@DynamoDBTable(tableName = "Dev")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Dev implements Serializable {
    private String devId;

    @DynamoDBHashKey(attributeName = "devId")
    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }
}

