package org.example.listener;

import org.example.config.RabbitConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMegListener {
    @RabbitListener(queues = RabbitConfig.QueueKey)
    public void onMessage(String message) {
        System.out.println("监听"+message);

    }
}
