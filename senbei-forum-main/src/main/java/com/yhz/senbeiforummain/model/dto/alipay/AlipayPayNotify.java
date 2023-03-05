package com.yhz.senbeiforummain.model.dto.alipay;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AlipayPayNotify extends AlipayNotifyCommon {
    @JSONField(name = "trade_no")
    private String tradeNo;
    @JSONField(name = "app_id")
    private String appId;
    @JSONField(name = "out_trade_no")
    private String outTradeNo;
    @JSONField(name = "out_biz_no")
    private String outBizNo;
    @JSONField(name = "buyer_id")
    private String buyerId;
    @JSONField(name = "seller_id")
    private String sellerId;
    @JSONField(name = "trade_status")
    private String tradeStatus;
    @JSONField(name = "total_amount")
    private BigDecimal totalAmount;
    @JSONField(name = "receipt_amount")
    private BigDecimal receiptAmount;
    @JSONField(name = "invoice_amount")
    private BigDecimal invoiceAmount;
    @JSONField(name = "buyer_pay_amount")
    private BigDecimal buyerPayAmount;
    @JSONField(name = "point_amount")
    private BigDecimal pointAmount;
    @JSONField(name = "refund_fee")
    private BigDecimal refundFee;
    private String subject;
    private String body;

    @JSONField(name = "gmt_create",format = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;
    @JSONField(name = "gmt_payment",format = "yyyy-MM-dd HH:mm:ss")
    private Date gmtPayment;
    @JSONField(name = "gmt_refund",format = "yyyy-MM-dd HH:mm:ss")
    private Date gmtRefund;
    @JSONField(name = "gmt_close",format = "yyyy-MM-dd HH:mm:ss")
    private Date gmtClose;
    @JSONField(name = "fund_bill_list")
    private String fundBillList;
    @JSONField(name = "vocher_detail_list",format = "yyyy-MM-dd HH:mm:ss")
    private String vocherDetailList;
    @JSONField(name = "passback_params",format = "yyyy-MM-dd HH:mm:ss")
    private String passbackParams;
}
