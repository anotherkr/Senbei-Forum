package com.yhz.senbeiforummain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.annotation.Resource;

/**
 * websocket配置类
 * @author 吉良吉影
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig {
    /**
     * 注入ServerEndpointExporter bean对象，自动注册使用了@ServerEndpoint注解的bean
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }


}