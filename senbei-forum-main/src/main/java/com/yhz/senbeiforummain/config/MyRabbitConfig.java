package com.yhz.senbeiforummain.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author yanhuanzhan
 * @date 2022/12/2 - 15:53
 */
@Configuration
@Slf4j
public class MyRabbitConfig {
    //@Resource
    RabbitTemplate rabbitTemplate;
    @Primary
    @Bean
    public RabbitTemplate myRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setMessageConverter(messageConverter());
        initRabbitTemplate();
        return rabbitTemplate;
    }
    /**
     * 使用JSON序列化机制,进行消息转换
     * @return
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    /**
     * 定制RabbitTemplate
     * 1,服务收到消息就回调
     *     1,spring.rabbitmq.publisher-confirm=true
     *     2,设置确认回调ConfirmCallback
     * 2,消息正确抵达队列进行回调
     *     1，spring.rabbitmq.publisher-return=true
     *       spring.rabbitmq.template.mandatory=true
     *     2,设置确认回调ReturnCallback
     * 3,消费端确认(保证每个消息被正确消费,此时才可以broker删除这个消息)
     *      1,默认是自动确认的，只要消息接收到，客户端会自动确认，服务端就会移除这个消息
     *          问题:我们收到很多消息，自动回复给服务器ack，只有一个消息处理成功，宕机了，发生消息丢失
     *          手动确认模式:只要我们没有明确告诉MQ，货物被签收，没有ack，消息就一直是unacked状态,即使Consumer宕机，
     *          消息不会丢失，会重新变为Ready，下一次有新的Consumer连接进来就发给他
     *      2，
     *
     */
    //@PostConstruct //MyRabbitConfig对象创建完成以后，执行这个方法
    public void initRabbitTemplate() {
        //设置ConfirmCallback
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             * 1,只要消息能抵达Broker就ack=true
             * @param correlationData 当前消息的唯一关联数据(这个是消息的唯一id)
             * @param ack 消息是否成功收到
             * @param cause 失败的原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                log.info("confirm...correlationData["+correlationData+"]==ack["+ack+"]");

            }
        });
        //设置消息抵达队列的确认回调
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            /**
             * 只要消息没有投递给指定的队列，就触发这个失败回调
             *
             * @param returnedMessage
             */
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage) {

            }
        });
    }
}
