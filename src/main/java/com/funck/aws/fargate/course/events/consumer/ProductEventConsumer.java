package com.funck.aws.fargate.course.events.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.funck.aws.fargate.course.events.model.Envelope;
import com.funck.aws.fargate.course.events.model.ProductEvent;
import com.funck.aws.fargate.course.events.model.SnsMessage;
import com.funck.aws.fargate.course.events.service.ProductEventLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.TextMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductEventConsumer {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final ProductEventLogService productEventLogService;

    @JmsListener(destination = "products-events")
    public void receiveProductEvent(final TextMessage message) throws Exception {
        final var snsMessage = MAPPER.readValue(message.getText(), SnsMessage.class);

        final var envelope = MAPPER.readValue(snsMessage.getMessage(), Envelope.class);

        final var productEvent = MAPPER.readValue(envelope.getData(), ProductEvent.class);

        log.info("Product event received from queue {}", productEvent);

        productEventLogService.save(envelope, productEvent);
    }

}
