package org.example.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String RouterKey="router_key";
    public static final String ExchangeKey="exchange_key";
    public static final String QueueKey="queue_key";
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(ExchangeKey);
    }

    @Bean
    public Queue queue() {
        return new Queue(QueueKey);
    }

    @Bean
    public Binding binding() {
        return BindingBuilder.bind(queue()).to(directExchange()).with(RouterKey);
    }
}
