package com.yhz.senbeiforummain.controller;

import com.yhz.commonutil.BaseResponse;
import com.yhz.commonutil.ResultUtils;
import com.yhz.senbeiforummain.domain.third.OssRequestParam;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.service.IOssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 阿里云对象存储
 * @author yanhuanzhan
 * @date 2022/11/22 - 15:27
 */
@RestController
@Api(tags = "阿里云对象存储")
@RequestMapping("/oss")
public class OssController {
    @Resource
    private IOssService ossService;
    @ApiOperation("客户端签名直传接口")
    @GetMapping("/policy")
    public BaseResponse<OssRequestParam> policy() throws BusinessException {
        OssRequestParam policy = ossService.policy();
        return ResultUtils.success(policy);
    }
}
