package com.yhz.senbeiforummain.controller;

import com.yhz.senbeiforummain.domain.Module;
import com.yhz.senbeiforummain.service.IModuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author yanhuanzhan
 * @date 2022/11/7 - 18:00
 */
@RestController
@Slf4j
@Api(tags = "hello world 测试")
public class HelloController {
    @Resource
    IModuleService moduleService;
    @GetMapping("/hello")
    @ApiOperation("hello接口")
    public String hello() {
        log.info("hello world");
        for (int i=1;i<10;i++){
            Module module = new Module();
            module.setName("模块" + i)
                    .setUserId(1L)
                    .setTopicNum(100L)
                    .setImgUrl("https://mail-yhz.oss-cn-guangzhou.aliyuncs.com/%E9%BB%91%E5%91%86.jpg");
            moduleService.save(module);
        }

        return "hello world";
    }
}
