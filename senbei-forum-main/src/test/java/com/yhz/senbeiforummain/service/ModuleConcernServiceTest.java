package com.yhz.senbeiforummain.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author yanhuanzhan
 * @date 2023/2/21 - 14:14
 */
@SpringBootTest
@Slf4j
class IModuleServiceTest {
    @Resource
    IModuleService moduleService;
    @Test
    void calibrationModuleConcern() {
        int i = moduleService.calibrationConcern();
        log.info("更新了{}条数据",i);
    }

    @Test
    void calibrationTopicNum() {
        int i = moduleService.calibrationTopicNum();
        log.info("更新了{}条数据",i);
    }
}