package com.funck.aws.fargate.course.events.config;

import com.amazon.sqs.javamessaging.ProviderConfiguration;
import com.amazon.sqs.javamessaging.SQSConnectionFactory;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.destination.DynamicDestinationResolver;

import javax.jms.Session;

@Profile("local")
@EnableJms
@Configuration
public class JmsConfigLocal {

    private final String awsRegion;

    public JmsConfigLocal(@Value("${aws.region}") String awsRegion) {
        this.awsRegion = awsRegion;
    }

    @Bean
    public SQSConnectionFactory connectionFactory() {
        return new SQSConnectionFactory(
                new ProviderConfiguration(),
                AmazonSQSClient.builder()
                        .withEndpointConfiguration(
                                new AwsClientBuilder.EndpointConfiguration(
                                        "http://localhost:4566",
                                        awsRegion))
                        .withCredentials(new DefaultAWSCredentialsProviderChain())
                        .build()
        );
    }

    @Bean
    public JmsListenerContainerFactory jmsListenerContainerFactory(SQSConnectionFactory connectionFactory) {
        var jmsListenerContainerFactory = new DefaultJmsListenerContainerFactory();

        jmsListenerContainerFactory.setConnectionFactory(connectionFactory);
        jmsListenerContainerFactory.setDestinationResolver(new DynamicDestinationResolver());
        jmsListenerContainerFactory.setConcurrency("2");
        jmsListenerContainerFactory.setSessionAcknowledgeMode(Session.CLIENT_ACKNOWLEDGE);

        return jmsListenerContainerFactory;
    }

}
