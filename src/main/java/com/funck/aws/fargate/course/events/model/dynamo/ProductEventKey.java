package com.funck.aws.fargate.course.events.model.dynamo;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
public class ProductEventKey {

    private String pk;
    private String sk;

    @DynamoDBHashKey(attributeName = "pk")
    public String getPk() {
        return pk;
    }

    @DynamoDBRangeKey(attributeName = "sk")
    public String getSk() {
        return sk;
    }
}
