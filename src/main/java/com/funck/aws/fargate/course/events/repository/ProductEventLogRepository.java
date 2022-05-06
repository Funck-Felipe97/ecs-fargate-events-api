package com.funck.aws.fargate.course.events.repository;

import com.funck.aws.fargate.course.events.model.dynamo.ProductEventKey;
import com.funck.aws.fargate.course.events.model.dynamo.ProductEventLog;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface ProductEventLogRepository extends CrudRepository<ProductEventLog, ProductEventKey> {

    List<ProductEventLog> findAllByPk(String pk);

    List<ProductEventLog> findAllByPkAndSkStartsWith(String pk, String eventType);

}
