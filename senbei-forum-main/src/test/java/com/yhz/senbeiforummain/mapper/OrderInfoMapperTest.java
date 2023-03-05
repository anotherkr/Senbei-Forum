package com.yhz.senbeiforummain.mapper;

import com.yhz.senbeiforummain.model.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author yanhuanzhan
 * @date 2023/3/3 - 17:16
 */
@SpringBootTest
@ActiveProfiles("dev")
class OrderInfoMapperTest {
    @Resource
    private OrderInfoMapper orderInfoMapper;
    @Test
    void createOrder() {
        Product product = new Product();
        product.setId(1L);
        product.setPrice(new BigDecimal(100));

        orderInfoMapper.createOrder(1L, "aaa", product, 1);
    }
}