package com.yhz.senbeiforummain.controller;

import com.alipay.api.AlipayApiException;
import com.yhz.commonutil.common.BaseResponse;
import com.yhz.commonutil.common.ResultUtils;
import com.yhz.senbeiforummain.common.annotation.UserId;
import com.yhz.senbeiforummain.service.IPayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 支付控制器
 * @author yanhuanzhan
 * @date 2023/3/3 - 16:37
 */
@Api(tags = "支付功能")
@RestController
@RequestMapping("/pay")
public class PayController {

    @Resource
    private IPayService payService;
    @ApiOperation("支付宝网站支付下单")
    @PostMapping("/preOrder/alipay-pc/{productId}")
    public BaseResponse alipayPcPreOrder(@PathVariable("productId") Long productId, @UserId Long userId) {
        String formStr=payService.preOrder(userId,productId);
        return ResultUtils.success(formStr);
    }
    @ApiOperation("支付宝支付回调接口")
    @PostMapping("/alipay/pay-notify")
    public String alipayAcceptNotify(@RequestParam Map<String, String> params) throws AlipayApiException {
        String result=payService.aliPayAcceptNotify(params);

        return result;
    }
}
