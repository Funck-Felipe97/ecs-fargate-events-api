package com.funck.aws.fargate.course.events.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.BillingMode;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ScalarAttributeType;
import com.funck.aws.fargate.course.events.repository.ProductEventLogRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Slf4j
@Profile("local")
@Configuration
@EnableDynamoDBRepositories(basePackageClasses = ProductEventLogRepository.class)
public class DynamodbConfigLocal {

    private final String awsRegion;

    public DynamodbConfigLocal(@Value("${aws.region}") final String awsRegion) {
        this.awsRegion = awsRegion;
    }

    @Bean
    @Primary
    public DynamoDBMapperConfig dynamoDBMapperConfig() {
        return DynamoDBMapperConfig.DEFAULT;
    }

    @Bean
    @Primary
    public DynamoDBMapper dynamoDBMapper(final AmazonDynamoDB amazonDynamoDB, final DynamoDBMapperConfig dynamoDBMapperConfig) {
        return new DynamoDBMapper(amazonDynamoDB, dynamoDBMapperConfig);
    }

    @Bean
    @Primary
    public AmazonDynamoDB amazonDynamoDB() {
        final var amazonDynamoDB = AmazonDynamoDBClient.builder().withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration(
                        "http://localhost:4566",
                        awsRegion))
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .build();

        //createTable(amazonDynamoDB);

        return amazonDynamoDB;
    }

    @SneakyThrows
    private void createTable(final AmazonDynamoDB amazonDynamoDB) {
        log.info("Creating products-events table");

        final var dynamoDB = new DynamoDB(amazonDynamoDB);

        final var attributeDefinitions = List.of(
                new AttributeDefinition().withAttributeName("pk").withAttributeType(ScalarAttributeType.S),
                new AttributeDefinition().withAttributeName("sk").withAttributeType(ScalarAttributeType.S)
        );

        final var keySchemaElements = List.of(
                new KeySchemaElement().withAttributeName("pk").withKeyType(KeyType.HASH),
                new KeySchemaElement().withAttributeName("sk").withKeyType(KeyType.RANGE)
        );

        final var createTableRequest = new CreateTableRequest()
                .withTableName("products-events")
                .withAttributeDefinitions(attributeDefinitions)
                .withKeySchema(keySchemaElements)
                .withBillingMode(BillingMode.PAY_PER_REQUEST);

        var table = dynamoDB.createTable(createTableRequest);

        table.waitForActive();

        log.info("products-events table created");
    }

}
