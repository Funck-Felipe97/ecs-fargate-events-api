package com.funck.aws.fargate.course.events.service;

import com.funck.aws.fargate.course.events.model.Envelope;
import com.funck.aws.fargate.course.events.model.ProductEvent;
import com.funck.aws.fargate.course.events.model.ProductEventLogResponse;
import com.funck.aws.fargate.course.events.model.dynamo.ProductEventLog;
import com.funck.aws.fargate.course.events.repository.ProductEventLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductEventLogService {

    private final ProductEventLogRepository productEventLogRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public void save(Envelope envelope, ProductEvent productEvent) {
        productEventLogRepository.save(buildProductEventLog(envelope, productEvent));
    }

    public List<ProductEventLogResponse> findAll() {
        return StreamSupport.stream(productEventLogRepository.findAll().spliterator(), false)
                .map(ProductEventLogResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductEventLogResponse> findAllByProductCode(final String productCode) {
        return productEventLogRepository.findAllByPk(productCode)
                .stream()
                .map(ProductEventLogResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductEventLogResponse> findAllByProductCodeAndEventType(final String productCode, final String eventType) {
        return productEventLogRepository.findAllByPkAndSkStartsWith(productCode, eventType)
                .stream()
                .map(ProductEventLogResponse::of)
                .collect(Collectors.toList());
    }

    public ProductEventLog buildProductEventLog(final Envelope envelope, final ProductEvent productEvent) {
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
