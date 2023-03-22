package com.yhz.senbeiforummain.service;

import com.yhz.senbeiforummain.model.entity.PaymentInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yhz.senbeiforummain.model.enums.PaymentTypeEnum;

/**
* @author 吉良吉影
* @description 针对表【payment_info】的数据库操作Service
* @createDate 2023-03-03 14:22:23
*/
public interface IPaymentInfoService extends IService<PaymentInfo> {

    /**
     * 记录支付日志
     * @param decryptData
     * @param aliPay
     */
    void createPaymentInfo(String decryptData, PaymentTypeEnum aliPay);
}
