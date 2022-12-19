package com.yhz.senbeiforummain;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableRabbit
@MapperScan("com.yhz.senbeiforummain.mapper")
@EnableTransactionManagement
public class SenbeiForumMainApplication {

    public static void main(String[] args) {
        SpringApplication.run(SenbeiForumMainApplication.class, args);
    }

}
