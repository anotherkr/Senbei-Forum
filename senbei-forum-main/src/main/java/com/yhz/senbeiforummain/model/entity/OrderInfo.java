package com.yhz.senbeiforummain.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.yhz.senbeiforummain.common.BaseEntity;
import lombok.Data;

/**
 * 
 * @TableName order_info
 */
@TableName(value ="order_info")
@Data
public class OrderInfo extends BaseEntity implements Serializable {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 商户订单编号
     */
    private String orderNo;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 商品id
     */
    private Long productId;

    /**
     * 订单金额
     */
    private Integer totalFee;

    /**
     * 订单状态(0:支付成功,1:转入退款,2:未支付,3:已关闭，4:支付失败,5:退款成功，6:退款失败）
     */
    private Integer orderStatus;

    /**
     * 支付类型(0:微信支付,1:支付宝支付)
     */
    private Integer paymentType;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}