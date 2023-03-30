package com.aummn.sqs.controller;

import com.aummn.sqs.exception.StockException;
import com.aummn.sqs.service.StockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
class SnsSubscribeController {

    @Autowired
    StockService stockService;

    @PostMapping(value = "/subscribe")
    public ResponseEntity<String> subscribe(@RequestHeader Map<String, String> headers) throws StockException {
        String topicName = headers.get("topic-name");
        String endpoint = headers.get("endpoint");
        stockService.subscribe(topicName, endpoint);
        return ResponseEntity.ok("topic " + topicName + " subscribed");
    }

}

