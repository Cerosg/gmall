package com.atguigu.gmall.search.listener;

import com.atguigu.gmall.search.service.SearchService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月04日 13时57分
 */
@Component
public class ItemListener {
    @Autowired
    private SearchService searchService;

    /**
     * 监听pms中的增、改操作
     *
     * @param spuId   增、改数据的id
     * @param channel 通道
     * @param message 消息
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            // 队列配置（队列名称，是否持久化）
            value = @Queue(value = "PMS-SAVE-QUEUE", durable = "true"),
            // 交换机配置（交换机名称，忽略声明异常，交换机类型）
            exchange = @Exchange(value = "PMS-ITEM-EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
            // RoutingKey：可以配置多个
            key = {"item.insert", "item.update"}
    ))
    public void listenerSave(Long spuId, Channel channel, Message message) throws IOException {
        if (spuId == null)
            return;
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        System.out.println(spuId); // 测试：打印消息
        // 创建索引库，实时更新数据
        searchService.createIndex(spuId);
    }

    /**
     * 监听pms中的删除操作
     *
     * @param spuId   删除数据的id
     * @param channel
     * @param message
     * @throws IOException
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = "PMS-DELETE-QUEUE", durable = "true"),
            exchange = @Exchange(value = "PMS-ITEM-EXCHANGE", ignoreDeclarationExceptions = "true", type = ExchangeTypes.TOPIC),
            key = {"item.delete"}
    ))
    public void listenerDelete(Long spuId, Channel channel, Message message) throws IOException {
    }
}
