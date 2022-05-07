package com.funck.aws.fargate.course.events.controller;

import com.funck.aws.fargate.course.events.model.ProductEventLogResponse;
import com.funck.aws.fargate.course.events.service.ProductEventLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/events")
public class ProductEventLogController {

    private final ProductEventLogService productEventLogService;

    @GetMapping
    public ResponseEntity<List<ProductEventLogResponse>> findAll() {
        return ResponseEntity.ok(productEventLogService.findAll());
    }

    @GetMapping("/{code}")
    public ResponseEntity<List<ProductEventLogResponse>> findAllByCode(@PathVariable final String code) {
        return ResponseEntity.ok(productEventLogService.findAllByProductCode(code));
    }

    @GetMapping("/{code}/type/{eventType}")
    public ResponseEntity<List<ProductEventLogResponse>> findAllByCodeAndEventType(@PathVariable final String code, @PathVariable final String eventType) {
        return ResponseEntity.ok(productEventLogService.findAllByProductCodeAndEventType(code, eventType));
    }

}
