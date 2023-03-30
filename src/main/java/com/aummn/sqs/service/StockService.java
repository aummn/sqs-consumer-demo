package com.aummn.sqs.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.SubscribeRequest;
import com.amazonaws.services.sns.model.SubscribeResult;
import com.aummn.sqs.exception.StockException;
import com.aummn.sqs.model.OrderInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.messaging.core.NotificationMessagingTemplate;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class StockService {

    @Autowired
    private AmazonSNS amazonSNS;
    @Autowired
    private AmazonS3 s3Client;

    @Autowired
    private SnsService snsService;

    @Autowired
    private ObjectMapper mapper;

    @Value("${order.queue.name}")
    private String orderQueueName;

    @Value("${order.bucket.name}")
    private String orderBucketName;

    @Autowired
    NotificationMessagingTemplate notificationMessagingTemplate;

    @SqsListener(value = "order", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void processMessage(OrderInfo orderInfo) throws JsonProcessingException {
        log.info("Message from SQS {}", orderInfo);

        String content = mapper.writeValueAsString(orderInfo);
        s3Client.putObject(orderBucketName, orderInfo.getOrderNo(), content);
    }

    public void processSnsMessage(String message) throws JsonProcessingException {
        log.info("process SNS message {}", message);
        OrderInfo orderInfo = deserializeOrderInfo(message);
        s3Client.putObject(orderBucketName, orderInfo.getOrderNo(), message);
        log.info("stored order {}", orderInfo.getOrderNo());
    }

    public void subscribe(String topicName, String endpoint) throws StockException {
        Optional<String> topicArnOpt = snsService.findTopicArnByName(topicName);
        String topicArn = topicArnOpt.orElseThrow(() -> new StockException(("Topic with name " + topicName + " not found.")));

        SubscribeRequest subscribeRequest = new SubscribeRequest(topicArn, HttpHost.DEFAULT_SCHEME_NAME, endpoint);
        SubscribeResult subscribeResult = amazonSNS.subscribe(subscribeRequest);
        System.out.println("Subscribed to topic " + topicName + " - subscription ARN: " + subscribeResult.getSubscriptionArn());
    }

    public OrderInfo deserializeOrderInfo(String jsonString) throws JsonProcessingException {
        return mapper.readValue(jsonString, OrderInfo.class);
    }
}
