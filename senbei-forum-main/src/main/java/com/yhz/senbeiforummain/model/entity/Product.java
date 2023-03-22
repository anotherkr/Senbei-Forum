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
 * @TableName product
 */
@TableName(value ="product")
@Data
public class Product extends BaseEntity implements Serializable {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 商品价格
     */
    private BigDecimal price;

    /**
     * 商品折扣价
     */
    private BigDecimal priceDiscount;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 商品图片
     */
    private String imgUrl;





    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}