package com.funck.aws.fargate.course.events.model.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.funck.aws.fargate.course.events.model.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamoDBTable(tableName = "products-events")
public class ProductEventLog {

    @Id
    private ProductEventKey productEventKey;

    @DynamoDBAttribute(attributeName = "product_id")
    private Long productId;

    @DynamoDBTypeConvertedEnum
    @DynamoDBAttribute(attributeName = "event_type")
    private EventType eventType;

    @DynamoDBAttribute(attributeName = "timestamp")
    private Long timestamp;

    @DynamoDBAttribute(attributeName = "ttl")
    private Long ttl;

    @DynamoDBHashKey(attributeName = "pk")
    public String getPk() {
        return productEventKey != null ? productEventKey.getPk() : null;
    }

    public void setPk(String pk) {
        if (productEventKey == null) {
            productEventKey = new ProductEventKey();
        }
        productEventKey.setPk(pk);
    }

    @DynamoDBRangeKey(attributeName = "sk")
    public String getSk() {
        return productEventKey != null ? productEventKey.getSk() : null;
    }

    public void setSk(String sk) {
        if (productEventKey == null) {
            productEventKey = new ProductEventKey();
        }
        productEventKey.setSk(sk);
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Long getTtl() {
        return ttl;
    }

    public void setTtl(Long ttl) {
        this.ttl = ttl;
    }
}
