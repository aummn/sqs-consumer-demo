package com.aummn.sqs.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.SQLException;
import java.time.LocalDateTime;

@Slf4j
@Service
public class RetryService {

    // @Retryable‘s default behavior, the retry may happen up to three times,
    // with a delay of one second between retries.
    @Retryable
    public void retryService() {
        System.out.println(" Making a call to method retryService() at :" + LocalDateTime.now());
        throw new RuntimeException();
    }

    // the retry is attempted when an SQLException is thrown
    @Retryable(value = SQLException.class)
    public void retryServiceWithRecovery(String sql) throws SQLException {
        System.out.println(" Making a call to method retryServiceWithRecovery() at :" + LocalDateTime.now());
        if (StringUtils.isEmpty(sql)) {
            throw new SQLException();
        }
    }

    // @Recover defines a recovery method
    // when a @Retryable method fails with a specified exception
    @Recover
    public void recovery(SQLException e, String sql) {
        log.info("In recovery method");
    }


    @Retryable(value = { SQLException.class }, maxAttempts = 4, backoff = @Backoff(delay = 5000))
    public void retryServiceWithCustomization(String sql) throws SQLException {
        System.out.println(" Making a call to method retryServiceWithCustomization() at :" + LocalDateTime.now());
        if (StringUtils.isEmpty(sql)) {
            throw new SQLException();
        }
    }

    @Retryable( value = SQLException.class, maxAttemptsExpression = "${retry.maxAttempts}",
            backoff = @Backoff(delayExpression = "${retry.maxDelay}"))
    public void retryServiceWithExternalConfiguration(String sql) throws SQLException {
        System.out.println(" Making a call to method retryServiceWithExternalConfiguration() at :" + LocalDateTime.now());
        if (StringUtils.isEmpty(sql)) {
            throw new SQLException();
        }
    }


    public void templateRetryService(String sql) throws SQLException {
        System.out.println(" Making a call to method templateRetryService() at :" + LocalDateTime.now());
        if (StringUtils.isEmpty(sql)) {
            throw new SQLException();
        }
    }
}
