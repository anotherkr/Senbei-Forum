package com.yhz.senbeiforummain.factory;

import com.yhz.senbeiforummain.handler.OauthLoginHandler;
import com.yhz.senbeiforummain.handler.PaymentHandler;
import com.yhz.senbeiforummain.model.enums.LoginChannelEnum;
import com.yhz.senbeiforummain.model.enums.PaymentTypeEnum;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 支付处理器工厂
 * @author yanhuanzhan
 * @date 2022/12/15 - 22:07
 */
@Component
public class PaymentFactory implements InitializingBean {
    @Resource
    private List<PaymentHandler> paymentHandlerList;
    private Map<PaymentTypeEnum, PaymentHandler> handlerMap = new HashMap<>();

    public PaymentHandler getHandlerByCode(int code) {
        PaymentTypeEnum paymentTypeEnum = PaymentTypeEnum.getByCode(code);
        return handlerMap.get(paymentTypeEnum);
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        paymentHandlerList.forEach(loginHandler -> {
            handlerMap.put(loginHandler.getChannel(), loginHandler);
        });
    }

    public PaymentHandler getHandler(PaymentTypeEnum paymentTypeEnum) {
        return handlerMap.get(paymentTypeEnum);
    }
}
