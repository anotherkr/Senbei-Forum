package com.yhz.senbeiforummain;

import com.yhz.senbeiforummain.model.entity.Topic;
import com.yhz.senbeiforummain.model.enums.QueueEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author yanhuanzhan
 * @date 2022/12/1 - 17:16
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
@ActiveProfiles("dev")
public class RabbitMqTest {
    @Resource
    private AmqpAdmin amqpAdmin;
    @Resource
    RabbitTemplate rabbitTemplate;
    @Test
    public void sendMessageTest() {
        Topic topic = new Topic();
        topic.setModuleId(1L);
        topic.setTitle("hello");
        //1,发送消息,如果发送的消息是个对象,我们会使用序列化机制将对象写出去,对象必须实现Serializable
        //发送的对象类型的消息可以是一个json

        rabbitTemplate.convertAndSend("hello-java-exchange", "hello.java", topic, new CorrelationData(UUID.randomUUID().toString()));
        log.info("消息发送完成");
    }
    //@Test
    //public void createExchange() {
    //    DirectExchange directExchange = new DirectExchange("hello-java-exchange", true, false);
    //    amqpAdmin.declareExchange(directExchange);
    //    log.info("exchange[{}]创建成功", directExchange.getName());
    //}
    //
    //@Test
    //public void createQueue() {
    //    Queue queue = new Queue("hello-java-queue", true, false, false);
    //    amqpAdmin.declareQueue(queue);
    //    log.info("queue[{}]创建成功",queue.getName());
    //}
    //@Test
    //public void createBinding() {
    //    //String destination 【目的地】
    //    //DestinationType destinationType【目的地类型】
    //    // String exchange【交换机】
    //    // String routingKey【路由键】
    //    Binding binding = new Binding("hello-java-queue",
    //            Binding.DestinationType.QUEUE,
    //            "hello-java-exchange",
    //            "hello.java",null);
    //    amqpAdmin.declareBinding(binding);
    //    log.info("Binding[{}]创建成功", "hello-java-binding");
    //}

    @Test
    public void testCancelOrderSend() {
        rabbitTemplate.convertAndSend(QueueEnum.QUEUE_TTL_ORDER_CANCEL.getExchange(),QueueEnum.QUEUE_TTL_ORDER_CANCEL.getRouteKey(),"abc");
    }
}
