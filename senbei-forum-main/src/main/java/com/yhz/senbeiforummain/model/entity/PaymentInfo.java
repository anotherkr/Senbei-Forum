package com.yhz.senbeiforummain.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.yhz.senbeiforummain.common.BaseEntity;
import lombok.Data;

/**
 * 
 * @TableName payment_info
 */
@TableName(value ="payment_info")
@Data
public class PaymentInfo extends BaseEntity implements Serializable {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 商品订单编号
     */
    private String orderNo;

    /**
     * 支付系统交易编号
     */
    private String transactionId;

    /**
     * 支付类型
     */
    private Integer paymentType;

    /**
     * 交易类型
     */
    private String tradeType;

    /**
     * 交易状态(0:支付成功,2:未支付,6:支付失败)
     */
    private Integer tradeState;

    /**
     * 支付金额
     */
    private BigDecimal payerTotal;

    /**
     * 通知参数
     */
    private String content;




    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}