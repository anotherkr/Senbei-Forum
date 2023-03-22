package com.yhz.senbeiforummain.mapper;

import com.yhz.senbeiforummain.model.entity.OrderInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yhz.senbeiforummain.model.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author 吉良吉影
* @description 针对表【order_info】的数据库操作Mapper
* @createDate 2023-03-03 14:22:23
* @Entity com.yhz.senbeiforummain.model.entity.OrderInfo
*/
@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {
    /**
     * 创建订单
     * @param userId
     * @param orderNo
     * @param product
     * @param paymentType
     * @return
     */
    int createOrder(@Param("userId")Long userId,@Param("orderNo") String orderNo,@Param("product") Product product,@Param("paymentType") int paymentType);
}




