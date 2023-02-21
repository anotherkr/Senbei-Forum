package com.yhz.senbeiforummain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbitmq.client.Channel;
import com.yhz.senbeiforummain.model.entity.Topic;
import com.yhz.senbeiforummain.model.entity.User;
import com.yhz.senbeiforummain.service.IUserService;
import com.yhz.senbeiforummain.mapper.UserMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
* @author 吉良吉影
* @description 针对表【user】的数据库操作Service实现
* @createDate 2022-11-13 16:08:53
*/
@Service
@RabbitListener(queues = {"hello-java-queue"})
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements IUserService {
    /**
     * queues:声明需要监听的所有队列
     * 参数可以写以下类型：
     * 1，Message message :消息详细信息
     * 2，T<发送的消息的类型>
     * 3,Channel channel:当前传输数据的通道
     *
     * Queue:可以很多人来监听，只要收到消息，队列就会删除消息，而且只能有一个收到此消息
     */
    //@RabbitListener(queues = {"hello-java-queue"})
    @RabbitHandler
    public void receiveMessage(Message message, Topic topic,
                               Channel channel) {
        byte[] body = message.getBody();
        //消息头属性信息
        MessageProperties messageProperties = message.getMessageProperties();
        System.out.println("接收到消息:"+message+",内容为:"+ topic);
        //channel 内按顺序自增的
        long deliveryTag = messageProperties.getDeliveryTag();
        //签收货物,非批量模式
        try {
            channel.basicAck(deliveryTag, false);
        } catch (IOException e) {
            //网络中断
        }
    }

    @Override
    public User getUserByUserName(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username);
        User user = baseMapper.selectOne(wrapper);
        return user;
    }
}




