package com.aummn.sqs.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.annotation.CircuitBreaker;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Slf4j
@Service
public class CustomerService {

	@Autowired
	RetryTemplate retryTemplate;

	/**
	 *  This method returns the customer name with circuit breaker support.
	 *
	 *  @return the customer name
	 */
	@CircuitBreaker(maxAttempts=3,openTimeout=10000l, resetTimeout=30000l)
	public String getCustomerNameWithCircuitBreaker() {
		return retryTemplate.execute(new RetryCallback<String, RuntimeException>() {
			@Override
			public String doWithRetry(RetryContext context) {
				System.out.println(" Making a call to method getCustomerName() at :" + LocalDateTime.now());
				log.info(String.format("Circuit Breaker Retry count %d", context.getRetryCount() + 1));
				return getCustomerName();
			}
		});
	}

	/**
	 *  This method returns the customer name.
	 *
	 *  @return the customer name
	 */
	public String getCustomerName() {
		ResponseEntity<String> exchange = new RestTemplate().exchange("http://localhost:8080/api/staff/name",
				HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
				});
		return exchange.getBody();
	}

	@Recover
	public String fallback(Throwable e) {
		log.info("in fallback method");
		return "James";
	}
	
}

