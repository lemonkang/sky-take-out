package org.example.listener;

import com.rabbitmq.client.Channel;
import org.example.config.RabbitConfig;
import org.example.dto.SeckillMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MessageListener {
    private static final Logger log = LoggerFactory.getLogger(MessageListener.class);

    @RabbitListener(queues = RabbitConfig.DLX_QUEUE)
    public void handleOrderTimeout(String orderId) {
        System.out.println("订单超时未支付，取消订单：" + orderId);
    }
    @RabbitListener(queues = RabbitConfig.Skill_QUEUE, concurrency = "10")
    public void handleSkill(SeckillMessage message, Channel channel,
                            @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            // 执行秒杀逻辑
           Thread.sleep(15000);
           log.info("秒杀执行",message.getProductId());
            // 手动确认
            channel.basicAck(tag, false);
        } catch (Exception e) {
            // 失败重新入队
            channel.basicNack(tag, false, true);
        }
    }


}
