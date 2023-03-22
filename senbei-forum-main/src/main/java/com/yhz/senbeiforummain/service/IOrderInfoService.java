package com.yhz.senbeiforummain.service;

import com.yhz.senbeiforummain.model.dto.alipay.AlipayPayNotify;
import com.yhz.senbeiforummain.model.entity.OrderInfo;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
