package com.yhz.senbeiforummain.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhz.senbeiforummain.model.dto.alipay.AlipayPayNotify;
import com.yhz.senbeiforummain.model.entity.PaymentInfo;
import com.yhz.senbeiforummain.model.enums.PaymentTypeEnum;
import com.yhz.senbeiforummain.model.enums.TradeStateEnum;
import com.yhz.senbeiforummain.service.IPaymentInfoService;
import com.yhz.senbeiforummain.mapper.PaymentInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;

/**
* @author 吉良吉影
* @description 针对表【payment_info】的数据库操作Service实现
* @createDate 2023-03-03 14:22:23
*/
@Service
@Slf4j
public class PaymentInfoServiceImpl extends ServiceImpl<PaymentInfoMapper, PaymentInfo>
    implements IPaymentInfoService {

    @Override
    public void createPaymentInfo(String decryptData, PaymentTypeEnum paymentTypeEnum) {
        PaymentInfo paymentInfo = new PaymentInfo();
       if (PaymentTypeEnum.ALI_PAY.equals(paymentTypeEnum)) {
            AlipayPayNotify payNotify = JSONObject.parseObject(decryptData, AlipayPayNotify.class);
            log.info("query resp:{}",payNotify);
            paymentInfo.setOrderNo(payNotify.getOutTradeNo());
            paymentInfo.setPaymentType(paymentTypeEnum.getCode());
            paymentInfo.setTradeType("PC");
            paymentInfo.setTradeState(TradeStateEnum.SUCCESS.getCode());
            paymentInfo.setTransactionId(payNotify.getTradeNo());
            paymentInfo.setPayerTotal(payNotify.getTotalAmount());
            paymentInfo.setContent(decryptData);
        }
        if (!ObjectUtils.isEmpty(paymentInfo)) {
            this.save(paymentInfo);
        }
    }
}




