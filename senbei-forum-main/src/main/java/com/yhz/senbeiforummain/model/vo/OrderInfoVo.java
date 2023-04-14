package com.yhz.senbeiforummain.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yanhuanzhan
 * @date 2023/4/10 - 19:57
 */
@Data
public class OrderInfoVo {
    private Long id;

    /**
     * 商户订单编号
     */
    @ApiModelProperty("订单编号")
    private String orderNo;

    /**
     * 商品id
     */
    private Long productId;

    @ApiModelProperty("商品名")
    private String productName;
    /**
     * 订单金额
     */
    @ApiModelProperty("订单金额")
    private BigDecimal totalFee;

    /**
     * 订单状态(0:支付成功,1:转入退款,2:未支付,3:已关闭，4:支付失败,5:退款成功，6:退款失败）
     */
    @ApiModelProperty("订单状态(0:支付成功,1:转入退款,2:未支付,3:已关闭，4:支付失败,5:退款成功，6:退款失败）")
    private Integer orderStatus;


}
