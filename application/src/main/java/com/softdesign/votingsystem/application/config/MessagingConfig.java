package com.softdesign.votingsystem.application.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessagingConfig {

    @Value("${queue.order.name}")
    private String votingResultQueue;

    @Bean
    public Queue queue() {
        return new Queue(votingResultQueue, true);
    }
}
