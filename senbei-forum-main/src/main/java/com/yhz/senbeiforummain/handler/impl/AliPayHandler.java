package com.yhz.senbeiforummain.handler.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeCloseModel;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.request.AlipayTradeCloseRequest;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.yhz.commonutil.common.ErrorCode;
import com.yhz.senbeiforummain.config.pay.AlipayConfig;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.handler.PaymentHandler;
import com.yhz.senbeiforummain.model.enums.PaymentTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @author yanhuanzhan
 * @date 2023/3/3 - 15:42
 */
@Slf4j
@Component
public class AliPayHandler implements PaymentHandler {

    @Resource
    private AlipayConfig alipayConfig;
    @Override
    public PaymentTypeEnum getChannel() {
        return PaymentTypeEnum.ALI_PAY;
    }

    @Override
    public String pcPreOrder(String orderNo, String productName, String notifyUrl, Integer totalFee) throws BusinessException {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(notifyUrl);
        //支付完成后跳转的界面
        request.setReturnUrl(alipayConfig.getReturnUrl());
        //组装当前业务方法的请求参数
        AlipayTradePagePayModel pagePayModel = new AlipayTradePagePayModel();
        pagePayModel.setOutTradeNo(orderNo);
        BigDecimal totalAmount = new BigDecimal(totalFee).divide(new BigDecimal(100));
        pagePayModel.setTotalAmount(totalAmount.toString());
        pagePayModel.setSubject(productName);
        pagePayModel.setProductCode("FAST_INSTANT_TRADE_PAY");
        request.setBizModel(pagePayModel);
        log.info("Alipay PreOrder req:{}",request);
        AlipayTradePagePayResponse response = null;
        try {
            response = this.getAlipayClient().pageExecute(request);
        } catch (AlipayApiException e) {
            log.error("支付宝支付下单失败,错误信息:{}",e);
        }
        if(response.isSuccess()){
            log.info("调用成功，返回结果 ===> " + response.getBody());
            return response.getBody();
        } else {
            log.info("调用失败，返回码 ===> " + response.getCode() + ", 返回描述 ===> " + response.getMsg());
            throw new RuntimeException("创建支付交易失败");
        }
    }

    @Override
    public void closeOrder(String orderNo) {
        AlipayTradeCloseRequest request = new AlipayTradeCloseRequest();
        AlipayTradeCloseModel model = new AlipayTradeCloseModel();
        model.setOutTradeNo(orderNo);
        request.setBizModel(model);
        AlipayTradeCloseResponse response = null;
        try {
            response = this.getAlipayClient().execute(request);
        } catch (AlipayApiException e) {
            log.error("支付宝关单接口调用失败:{}",e);
            throw new BusinessException(ErrorCode.HTTP_REQ_ERROR);
        }
        if (response.isSuccess()) {
            log.info("订单号:{},关单接口调用成功",orderNo);
        }else {
            log.info("订单号:{},关单接口调用失败",orderNo);
        }
    }

    public AlipayClient getAlipayClient() {
        DefaultAlipayClient alipayClient = new DefaultAlipayClient(alipayConfig.getGatewayUrl()
                ,alipayConfig.getAppid()
                ,alipayConfig.getMerchantPrivateKey()
                ,"json"
                , AlipayConstants.CHARSET_UTF8
                ,alipayConfig.getAlipayPublicKey()
                ,AlipayConstants.SIGN_TYPE_RSA2
        );
        return alipayClient;
    }
    @Override
    public AlipayTradeQueryResponse queryOrder(String orderNo) {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        AlipayTradeQueryModel model = new AlipayTradeQueryModel();
        model.setOutTradeNo(orderNo);
        request.setBizModel(model);
        AlipayTradeQueryResponse response = null;
        try {
            response = this.getAlipayClient().execute(request);
        } catch (AlipayApiException e) {
            log.error("支付宝查单接口调用失败");
            throw new BusinessException(ErrorCode.HTTP_REQ_ERROR);
        }
        if (response.isSuccess()) {
            log.info("支付宝查单接口调用成功");
            return response;
        } else {
            log.info("调用支付宝查单接口失败,返回码:{},返回信息描述:{}",response.getCode(),response.getMsg());
            return null;
        }
    }
}
