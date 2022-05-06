package com.funck.aws.fargate.course.events.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.funck.aws.fargate.course.events.model.Envelope;
import com.funck.aws.fargate.course.events.model.ProductEvent;
import com.funck.aws.fargate.course.events.model.SnsMessage;
import com.funck.aws.fargate.course.events.model.dynamo.ProductEventLog;
import com.funck.aws.fargate.course.events.repository.ProductEventLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.TextMessage;
import java.time.Duration;
import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductEventConsumer {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final ProductEventLogRepository productEventLogRepository;

    @JmsListener(destination = "products-events")
    public void receiveProductEvent(TextMessage message) throws Exception {
        var snsMessage = MAPPER.readValue(message.getText(), SnsMessage.class);

        var envelope = MAPPER.readValue(snsMessage.getMessage(), Envelope.class);

        var productEvent = MAPPER.readValue(envelope.getData(), ProductEvent.class);

        log.info("Product event received from queue {}", productEvent);

        productEventLogRepository.save(buildProductEventLog(envelope, productEvent));
    }

    public ProductEventLog buildProductEventLog(Envelope envelope, ProductEvent productEvent) {
        long timestamp = Instant.now().toEpochMilli();

        var productEventLog = ProductEventLog.builder()
                .ttl(Instant.now().plus(Duration.ofMinutes(10)).toEpochMilli())
                .productId(productEvent.getProductId())
                .eventType(envelope.getEventType())
                .timestamp(timestamp)
                .build();

        productEventLog.setPk(productEvent.getCode());
        productEventLog.setSk(envelope.getEventType() + "_" + timestamp);

        return productEventLog;
    }

}
