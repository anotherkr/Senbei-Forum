package com.yhz.senbeiforummain.mq;

import com.alipay.api.AlipayApiException;
import com.rabbitmq.client.Channel;
import com.yhz.commonutil.common.ErrorCode;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.model.entity.OrderInfo;
import com.yhz.senbeiforummain.model.entity.Product;
import com.yhz.senbeiforummain.model.enums.TradeStateEnum;
import com.yhz.senbeiforummain.service.IOrderInfoService;
import com.yhz.senbeiforummain.service.IPayService;
import com.yhz.senbeiforummain.service.IProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author yanhuanzhan
 * @date 2023/3/30 - 18:49
 */
@Component
@RabbitListener(queues = "order-create-queue")
@Slf4j
public class CreateOrderListener {
    @Resource
    private IOrderInfoService orderInfoService;

    @RabbitHandler
    public void handle(OrderInfo orderInfo, Channel channel, Message message) throws IOException {
        MessageProperties messageProperties = message.getMessageProperties();
        if (orderInfo != null) {
            orderInfoService.createOrder(orderInfo);
        }
        try {
            //手动确定消息
            channel.basicAck(messageProperties.getDeliveryTag(),false);
        } catch (IOException e) {
            log.error("手动确认消息失败");
            //拒绝这条消息
            //如果 requeue 设置为 true，则消息会被重新放回队列中等待被重新分发给消费者；如果设置为 false，则消息会被丢弃。
            channel.basicReject(messageProperties.getDeliveryTag(),false);
        }
    }

}
