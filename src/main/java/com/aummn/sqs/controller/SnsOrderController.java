package com.aummn.sqs.controller;

import com.aummn.sqs.service.StockService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.awspring.cloud.messaging.config.annotation.NotificationMessage;
import io.awspring.cloud.messaging.config.annotation.NotificationSubject;
import io.awspring.cloud.messaging.endpoint.NotificationStatus;
import io.awspring.cloud.messaging.endpoint.annotation.NotificationMessageMapping;
import io.awspring.cloud.messaging.endpoint.annotation.NotificationSubscriptionMapping;
import io.awspring.cloud.messaging.endpoint.annotation.NotificationUnsubscribeConfirmationMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/order")
class SnsOrderController {

	@Autowired
	StockService stockService;

	@Autowired
	private ObjectMapper mapper;

	@NotificationSubscriptionMapping
	public void handleSubscriptionMessage(NotificationStatus status) {
		log.info("We subscribe to start receive the message");
		status.confirmSubscription();
	}

	@NotificationMessageMapping
	public void handleNotificationMessage(@NotificationSubject String subject, @NotificationMessage String message) throws JsonProcessingException {
		log.info("handleNotificationMessage {}", message);
		stockService.processSnsMessage(message);
	}

	@NotificationUnsubscribeConfirmationMapping
	public void handleUnsubscribeMessage(NotificationStatus status) {
		log.info("the client has been unsubscribed");
		status.confirmSubscription();
	}

}

