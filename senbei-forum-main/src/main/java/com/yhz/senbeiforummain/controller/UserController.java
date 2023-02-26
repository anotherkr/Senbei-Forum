package com.yhz.senbeiforummain.controller;

import com.yhz.commonutil.common.BaseResponse;
import com.yhz.commonutil.common.ResultUtils;
import com.yhz.senbeiforummain.model.dto.register.EmailRegisterRequest;
import com.yhz.senbeiforummain.model.dto.login.DoLoginRequest;
import com.yhz.senbeiforummain.model.entity.User;
import com.yhz.senbeiforummain.model.vo.CaptchaImageVo;
import com.yhz.senbeiforummain.model.vo.UserInfoVo;
import com.yhz.senbeiforummain.service.ILoginService;
import com.yhz.senbeiforummain.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


/**
 * @author 吉良吉影
 */
@Api(tags = "用户")
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private ILoginService loginService;
    @Resource
    private IUserService userService;


    @ApiOperation("用户登录接口")
    @PostMapping("/login")
    public BaseResponse<String> doLogin(@Valid @RequestBody DoLoginRequest doLoginRequest, String rememberMe, HttpServletRequest request, HttpServletResponse response) {
        String token = loginService.doLogin(doLoginRequest,request,response);

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
    @GetMapping("/captchaImage")
    @ApiOperation(value = "获取验证码（数学表达式）")
    public BaseResponse captchaImage() {
        CaptchaImageVo captchaImageVo = loginService.createCaptchImage();
        return ResultUtils.success(captchaImageVo);
    }

    @GetMapping("/info")
    @ApiOperation(value = "获取用户信息")
    public BaseResponse getUserInfo(HttpServletRequest request) {
        UserInfoVo userInfoVo=userService.getUserInfoByToken(request);
        return ResultUtils.success(userInfoVo);
    }
}

