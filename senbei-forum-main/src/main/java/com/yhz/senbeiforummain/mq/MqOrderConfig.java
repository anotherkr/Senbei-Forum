package com.yhz.senbeiforummain.mq;

import com.yhz.senbeiforummain.model.enums.QueueEnum;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 吉良吉影
 */
@Configuration
public class MqOrderConfig {

    /**
     * 订单消息实际消费队列所绑定的交换机
     */
    @Bean
    DirectExchange orderCancelDirect() {
        return ExchangeBuilder
                .directExchange(QueueEnum.QUEUE_ORDER_CANCEL.getExchange())
                .durable(true)
                .build();
    }

    /**
     * 订单延迟队列队列所绑定的交换机
     */
    @Bean
    DirectExchange orderTtlDirect() {
        return ExchangeBuilder
                .directExchange(QueueEnum.QUEUE_TTL_ORDER_CANCEL.getExchange())
                .durable(true)
                .build();
    }

    /**
     * 订单取消死信队列
     */
    @Bean
    public Queue orderCancelQueue() {
        return QueueBuilder.durable(QueueEnum.QUEUE_ORDER_CANCEL.getName()).build();
    }

    /**
     * 订单取消ttl队列
     */
    @Bean
    public Queue orderTtlQueue() {
        return QueueBuilder
                .durable(QueueEnum.QUEUE_TTL_ORDER_CANCEL.getName())
                //到期后转发的交换机
                .deadLetterExchange(QueueEnum.QUEUE_ORDER_CANCEL.getExchange())
                //到期后转发的路由键
                .deadLetterRoutingKey(QueueEnum.QUEUE_ORDER_CANCEL.getRouteKey())
                //设置过期时间,单位毫秒
                .ttl(1000*60*30)
                .build();
    }

    /**
     * 将订单取消死信队列绑定到交换机
     */
    @Bean
    Binding orderBinding(DirectExchange orderCancelDirect, Queue orderCancelQueue) {
        return BindingBuilder
                .bind(orderCancelQueue)
                .to(orderCancelDirect)
                .with(QueueEnum.QUEUE_ORDER_CANCEL.getRouteKey());
    }

    /**
     * 将订单延迟队列绑定到交换机
     */
    @Bean
    Binding orderTtlBinding(DirectExchange orderTtlDirect, Queue orderTtlQueue) {
        return BindingBuilder
                .bind(orderTtlQueue)
                .to(orderTtlDirect)
                .with(QueueEnum.QUEUE_TTL_ORDER_CANCEL.getRouteKey());
    }

    /**
     * 订单创建队列
     */
    @Bean
    public Queue orderCreateQueue() {
        return QueueBuilder.durable(QueueEnum.QUEUE_ORDER_CREATE.getName()).build();
    }

    /**
     * 订单创建 队列所绑定的交换机
     */
    @Bean
    DirectExchange orderCreateDirect() {
        return ExchangeBuilder
                .directExchange(QueueEnum.QUEUE_ORDER_CREATE.getExchange())
                .durable(true)
                .build();
    }

    /**
     * 将订单创建绑定到交换机
     */
    @Bean
    Binding orderCreateBinding(DirectExchange orderCreateDirect, Queue orderCreateQueue) {
        return BindingBuilder
                .bind(orderCreateQueue)
                .to(orderCreateDirect)
                .with(QueueEnum.QUEUE_ORDER_CREATE.getRouteKey());
    }
}