package com.yhz.senbeiforummain.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yhz.commonutil.common.BaseResponse;
import com.yhz.commonutil.common.PageRequest;
import com.yhz.commonutil.common.ResultUtils;
import com.yhz.senbeiforummain.common.annotation.UserId;
import com.yhz.senbeiforummain.model.enums.PaymentTypeEnum;
import com.yhz.senbeiforummain.model.vo.OrderInfoVo;
import com.yhz.senbeiforummain.service.IOrderInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author yanhuanzhan
 * @date 2023/4/10 - 19:52
 */
@RestController
@RequestMapping("/order")
@Api(tags = "订单模块")
public class OrderController {

    @Resource
    private IOrderInfoService orderInfoService;
    @ApiOperation("订单分页接口")
    @PostMapping("/page")
    public BaseResponse getOrderInfo(@RequestBody PageRequest pageRequest, @UserId Long userId) {
        IPage<OrderInfoVo> iPage = orderInfoService.getOrderInfoVoByPage(pageRequest, userId);
        return ResultUtils.success(iPage);
    }

    @GetMapping("/create/{productId}")
    @ApiOperation("创建订单接口")
    public BaseResponse<String> createOrder(@PathVariable("productId") Long productId, @UserId Long userId) {
        String orderNo = orderInfoService.getOrderNo(productId, userId, PaymentTypeEnum.ALI_PAY);
        return ResultUtils.success(orderNo);
    }
}
