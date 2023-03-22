package com.yhz.senbeiforummain.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yhz.senbeiforummain.model.dto.product.ProductQueryRequest;
import com.yhz.senbeiforummain.model.entity.Product;
import com.yhz.senbeiforummain.model.vo.ProductVo;
import com.yhz.senbeiforummain.service.IProductService;
import com.yhz.senbeiforummain.mapper.ProductMapper;
import com.yhz.senbeiforummain.util.PageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

/**
 * @author 吉良吉影
 * @description 针对表【product】的数据库操作Service实现
 * @createDate 2023-03-03 14:22:23
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
        implements IProductService {

    @Override
    public IPage<ProductVo> pageList(ProductQueryRequest productQueryRequest) {
        Long current = productQueryRequest.getCurrent();
        Long pageSize = productQueryRequest.getPageSize();
        String sortField = productQueryRequest.getSortField();
        String sortOrder = productQueryRequest.getSortOrder();
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        PageUtil.dealSortWrapper(queryWrapper, sortField, sortOrder);
        IPage<Product> productIPage = PageUtil.vaildPageParam(current, pageSize);
        IPage<Product> page = this.page(productIPage, queryWrapper);
        IPage<ProductVo> productVoIPage = page.convert(item -> {
            ProductVo productVo = new ProductVo();
            BeanUtils.copyProperties(item, productVo);
            return productVo;
        });
        return productVoIPage;
    }
}




