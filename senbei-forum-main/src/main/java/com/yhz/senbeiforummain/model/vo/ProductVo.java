package com.yhz.senbeiforummain.model.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author yanhuanzhan
 * @date 2023/2/19 - 19:53
 */
@Data
@ApiModel("商品信息展示项")
public class ProductVo {

    private Long id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品描述
     */
    @ApiModelProperty("商品描述")
    private String description;

    /**
     * 商品价格
     */
    @ApiModelProperty("原价")
    private BigDecimal price;

    /**
     * 商品折扣价
     */
    @ApiModelProperty("折扣价")
    private BigDecimal priceDiscount;

    /**
     * 库存
     */
    @ApiModelProperty("库存")
    private Integer stock;

    /**
     * 商品图片
     */
    @ApiModelProperty("商品图片")
    private String imgUrl;

}
