package com.yhz.senbeiforummain.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yanhuanzhan
 * @date 2022/11/7 - 18:00
 */
@RestController
@Slf4j
public class HelloController {
    @GetMapping("/hello")
    public String hello() {
        log.info("hello world");
        return "hello world";
    }
}
