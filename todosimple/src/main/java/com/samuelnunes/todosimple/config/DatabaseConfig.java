package com.samuelnunes.todosimple.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import javax.sql.DataSource;

@Configuration
@EnableRetry
public class DatabaseConfig {

    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        // Configure backoff policy
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(500);
        backOffPolicy.setMultiplier(2.0);
        backOffPolicy.setMaxInterval(30000);
        retryTemplate.setBackOffPolicy(backOffPolicy);

        // Configure retry policy
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(5);
        retryTemplate.setRetryPolicy(retryPolicy);

        return retryTemplate;
    }

    @Bean
    @Retryable(value = { DataAccessException.class }, maxAttempts = 5)
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
