package com.yhz.senbeiforummain;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@EnableRabbit
@MapperScan("com.yhz.senbeiforummain.mapper")
public class SenbeiForumMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(SenbeiForumMainApplication.class, args);
    }

}
