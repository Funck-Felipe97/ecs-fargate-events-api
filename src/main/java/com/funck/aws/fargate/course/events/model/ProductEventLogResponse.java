package com.funck.aws.fargate.course.events.model;

import com.funck.aws.fargate.course.events.model.dynamo.ProductEventLog;
import lombok.Getter;

@Getter
public class ProductEventLogResponse {

    private String code;
    private Long productId;
    private EventType eventType;
    private Long timestamp;

    private ProductEventLogResponse() {
    }

    public static ProductEventLogResponse of(ProductEventLog productEventLog) {
        var product = new ProductEventLogResponse();

        product.code = productEventLog.getPk();
        product.productId = productEventLog.getProductId();
        product.eventType = productEventLog.getEventType();
        product.timestamp = productEventLog.getTimestamp();

        return product;
    }

}
