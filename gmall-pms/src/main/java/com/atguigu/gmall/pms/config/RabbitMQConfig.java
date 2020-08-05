package com.atguigu.gmall.pms.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月04日 13时11分
 */
@Configuration
@Slf4j
public class RabbitMQConfig {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        // 确认消息是否到达交换机
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                log.error("消息未能到达交换机");
            }
        });
        // 确认是否到达队列，消息未到达队列才会执行
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            log.error("消息未能到达队列：交换机{}；路由键{}；消息内容{}", exchange, routingKey, message.getBody());
        });
    }
}
