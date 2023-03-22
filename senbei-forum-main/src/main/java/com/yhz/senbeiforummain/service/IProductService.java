package com.yhz.senbeiforummain.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yhz.senbeiforummain.model.dto.product.ProductQueryRequest;
import com.yhz.senbeiforummain.model.entity.Product;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yhz.senbeiforummain.model.vo.ProductVo;

/**
* @author 吉良吉影
* @description 针对表【product】的数据库操作Service
* @createDate 2023-03-03 14:22:23
*/
public interface IProductService extends IService<Product> {

    IPage<ProductVo> pageList(ProductQueryRequest productQueryRequest);
}
