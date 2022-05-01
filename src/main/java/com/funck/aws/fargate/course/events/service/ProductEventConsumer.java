package com.funck.aws.fargate.course.events.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.funck.aws.fargate.course.events.model.Envelope;
import com.funck.aws.fargate.course.events.model.ProductEvent;
import com.funck.aws.fargate.course.events.model.SnsMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.TextMessage;

@Slf4j
@Service
public class ProductEventConsumer {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @JmsListener(destination = "products-events")
    public void receiveProductEvent(TextMessage message) throws Exception {
        var snsMessage = MAPPER.readValue(message.getText(), SnsMessage.class);

        var envelope = MAPPER.readValue(snsMessage.getMessage(), Envelope.class);

        var productEvent = MAPPER.readValue(envelope.getData(), ProductEvent.class);

        log.info("Product event received from queue {}", productEvent);
    }

}
