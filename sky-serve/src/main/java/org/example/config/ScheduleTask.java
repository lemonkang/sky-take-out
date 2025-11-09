package org.example.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTask {
    @Autowired
    RabbitTemplate rabbitTemplate;
//    @Scheduled(fixedRate = 50000,initialDelay = 0)
//    public void scheduledTask() {
//        System.out.println("scheduledTask");
//        rabbitTemplate.convertAndSend(RabbitConfig.ExchangeKey,RabbitConfig.RouterKey,"hello",message->{
//            message.getMessageProperties().setExpiration(String.valueOf(20000));
//            return message;
//        });
//    }
}
