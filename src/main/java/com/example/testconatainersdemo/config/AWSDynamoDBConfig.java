package com.example.testconatainersdemo.config;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class to initialize DynamoDB clients
 */
@Configuration
public class AWSDynamoDBConfig {
    @Value("${aws.dynamoDb.endpoint}")
    String endpoint;
    @Value("${aws.region}")
    private String region;

    @Bean
    public AmazonDynamoDB dynamoDBClient() {
        return AmazonDynamoDBClientBuilder
            .standard()
            .withCredentials(new EnvironmentVariableCredentialsProvider())
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region)).build();
    }

    @Bean
    public DynamoDBMapper dynamoDBMapper(AmazonDynamoDB dynamoDBClient, DynamoDBMapperConfig dynamoDBMapperConfig) {
        return new DynamoDBMapper(dynamoDBClient, dynamoDBMapperConfig);
    }

    @Bean
    public DynamoDBMapperConfig dynamoDBMapperConfig() {
        return DynamoDBMapperConfig.builder()
            .withSaveBehavior(DynamoDBMapperConfig.SaveBehavior.CLOBBER)
            .withConsistentReads(DynamoDBMapperConfig.ConsistentReads.CONSISTENT)
            .withTableNameOverride(null)
            .withPaginationLoadingStrategy(DynamoDBMapperConfig.PaginationLoadingStrategy.EAGER_LOADING)
            .build();
    }
}
