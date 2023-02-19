package com.yhz.senbeiforummain.controller;

import com.yhz.commonutil.common.BaseResponse;
import com.yhz.commonutil.common.ResultUtils;
import com.yhz.senbeiforummain.model.dto.register.EmailRegisterRequest;
import com.yhz.senbeiforummain.model.dto.login.DoLoginRequest;
import com.yhz.senbeiforummain.service.ILoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;



/**
 * @author 吉良吉影
 */
@Api(tags = "用户模块")
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private ILoginService loginService;



    @ApiOperation("用户登录接口")
    @PostMapping("/login")
    public BaseResponse<String> doLogin(@Valid @RequestBody DoLoginRequest doLoginRequest) {

        String token = loginService.doLogin(doLoginRequest);
        return ResultUtils.success(token);
    }

    @ApiOperation("用户注销登录接口")
    @GetMapping("/logout")
    public BaseResponse doLogout() {
        loginService.doLogout();
        return ResultUtils.success();
    }
    @ApiOperation("邮箱验证码发送接口")
    @GetMapping("/email/send")
    public BaseResponse mailSend(@RequestParam("email") String email) {
        loginService.mailSend(email);
        return ResultUtils.success();
    }

    @ApiOperation("邮箱注册接口")
    @PostMapping("/register/email")
    public BaseResponse register(@Valid @RequestBody EmailRegisterRequest registerDto) {
        loginService.emailRegister(registerDto);
        return ResultUtils.success();
    }

}

