package com.aummn.sqs.service;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.ListTopicsRequest;
import com.amazonaws.services.sns.model.ListTopicsResult;
import com.amazonaws.services.sns.model.Topic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class SnsService {

    @Autowired
    private AmazonSNS amazonSNS;

    public Optional<String> findTopicArnByName(String topicName) {
        String nextToken = null;
        do {
            ListTopicsRequest request = new ListTopicsRequest().withNextToken(nextToken);
            ListTopicsResult result = amazonSNS.listTopics(request);
            nextToken = result.getNextToken();

            for (Topic topic : result.getTopics()) {
                String arn = topic.getTopicArn();
                if (arn.endsWith(":" + topicName)) {
                    return Optional.of(arn);
                }
            }
        } while (nextToken != null);

        return Optional.empty();
    }
}
