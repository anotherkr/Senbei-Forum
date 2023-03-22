package com.yhz.senbeiforummain.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yhz.commonutil.common.BaseResponse;
import com.yhz.commonutil.common.ResultUtils;
import com.yhz.senbeiforummain.common.annotation.UserId;
import com.yhz.senbeiforummain.model.dto.module.ModuleQueryRequest;
import com.yhz.senbeiforummain.model.dto.product.ProductQueryRequest;
import com.yhz.senbeiforummain.model.vo.ModuleVo;
import com.yhz.senbeiforummain.model.vo.ProductVo;
import com.yhz.senbeiforummain.service.IModuleService;
import com.yhz.senbeiforummain.service.IProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author yanhuanzhan
 * @date 2022/11/13 - 18:03
 */
@RestController
@Slf4j
@Api(tags = "商品")
@RequestMapping("/product")
public class ProductController {
    @Resource
    public IProductService productService;

    /**
     * 分页查询接口
     *
     * @return
     */
    @ApiOperation("分页查询接口")
    @PostMapping("/page")
    public BaseResponse<IPage<ProductVo>> pageList(@RequestBody ProductQueryRequest productQueryRequest) {
        IPage<ProductVo> page = productService.pageList(productQueryRequest);
        return ResultUtils.success(page);
    }

}
