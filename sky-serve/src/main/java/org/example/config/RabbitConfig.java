package org.example.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;


@Configuration
public class RabbitConfig {

        // ============ 交换机名称 ============
        public static final String NORMAL_EXCHANGE = "order-normal-exchange";
        public static final String DLX_EXCHANGE = "order-dlx-exchange";
    public static final String Skill_EXCHANGE = "Skill_EXCHANGE";

        // ============ 队列名称 ============
        public static final String DELAY_QUEUE = "order-delay-queue";
        public static final String DLX_QUEUE = "order-dlx-queue";
        public static final String Skill_QUEUE = "Skill_QUEUE ";
        // ============ routing key ============
        public static final String NORMAL_ROUTING_KEY = "order.create";
        public static final String DLX_ROUTING_KEY = "order.timeout";
        public static final String Skill_ROUTING_KEY = "Skill_ROUTING_KEY";

        // 1. 创建正常交换机
        @Bean
        public DirectExchange normalExchange() {
            return new DirectExchange(NORMAL_EXCHANGE, true, false);
        }

    @Bean
    public DirectExchange skillExchange() {
        return new DirectExchange(Skill_EXCHANGE, true, false);
    }

        // 2. 创建死信交换机
        @Bean
        public DirectExchange dlxExchange() {
            return new DirectExchange(DLX_EXCHANGE, true, false);
        }

        // 3. 延迟队列（设置死信交换机、死信routing-key）
        @Bean
        public Queue delayQueue() {
            Map<String, Object> args = new HashMap<>();
            args.put("x-dead-letter-exchange", DLX_EXCHANGE);
            args.put("x-dead-letter-routing-key", DLX_ROUTING_KEY);
            return new Queue(DELAY_QUEUE, true, false, false, args);
        }

        // 4. 死信队列
        @Bean
        public Queue dlxQueue() {
            return new Queue(DLX_QUEUE, true);
        }
    @Bean
    public Queue skillQueue() {
        return new Queue(Skill_QUEUE, true);
    }

        // 5. 延迟队列绑定正常交换机
        @Bean
        public Binding delayQueueBinding() {
            return BindingBuilder.bind(delayQueue())
                    .to(normalExchange())
                    .with(NORMAL_ROUTING_KEY);
        }
    @Bean
    public Binding skillQueueBinding() {
        return BindingBuilder.bind(skillQueue())
                .to(skillExchange())
                .with(Skill_ROUTING_KEY);
    }


        // 6. 死信队列绑定死信交换机
        @Bean
        public Binding dlxQueueBinding() {
            return BindingBuilder.bind(dlxQueue())
                    .to(dlxExchange())
                    .with(DLX_ROUTING_KEY);
        }
    }


