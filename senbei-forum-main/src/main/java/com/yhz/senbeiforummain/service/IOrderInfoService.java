package com.yhz.senbeiforummain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yhz.commonutil.common.PageRequest;
import com.yhz.senbeiforummain.model.dto.alipay.AlipayPayNotify;
import com.yhz.senbeiforummain.model.entity.OrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yhz.senbeiforummain.model.enums.PaymentTypeEnum;
import com.yhz.senbeiforummain.model.vo.OrderInfoVo;

/**
* @author 吉良吉影
* @description 针对表【order_info】的数据库操作Service
* @createDate 2023-03-03 14:22:23
*/
public interface IOrderInfoService extends IService<OrderInfo> {

    Boolean checkAlipayPayNotify(AlipayPayNotify payNotify);

    OrderInfo getByOrderNo(String outTradeNo);

    /**
     * 处理支付成功情况,记录支付日志
     * @param payNotify
     */
    void processAlipayPayNotify(AlipayPayNotify payNotify);

    IPage<OrderInfoVo> getOrderInfoVoByPage(PageRequest pageRequest, Long userId);


    /**
     * 创建并返回订单编号，给消息队列发送创建订单的消息
     * @param productId
     * @param userId
     * @param paymentTypeEnum 支付类型
     * @return
     */
    String getOrderNo(Long productId, Long userId, PaymentTypeEnum paymentTypeEnum);

    /**
     * 创建订单
     */
    void createOrder(OrderInfo orderInfo);
}
