package com.yhz.senbeiforummain.controller;

import com.yhz.commonutil.common.BaseResponse;
import com.yhz.commonutil.common.ErrorCode;
import com.yhz.commonutil.common.ResultUtils;
import com.yhz.senbeiforummain.common.annotation.UserId;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.model.dto.register.EmailRegisterRequest;
import com.yhz.senbeiforummain.model.dto.login.DoLoginRequest;
import com.yhz.senbeiforummain.model.dto.user.UserUpdateRequest;
import com.yhz.senbeiforummain.model.entity.User;
import com.yhz.senbeiforummain.model.vo.CaptchaImageVo;
import com.yhz.senbeiforummain.model.vo.UserInfoVo;
import com.yhz.senbeiforummain.service.ILoginService;
import com.yhz.senbeiforummain.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    @ApiOperation(value = "通过authentication获取用户信息")
    public BaseResponse getUserInfo(@UserId Long userId) {
        User user = userService.getById(userId);
        UserInfoVo userInfoVo = new UserInfoVo();
        if (user != null) {
            BeanUtils.copyProperties(user,userInfoVo);
        }
        return ResultUtils.success(userInfoVo);
    }
    @GetMapping("/info/{userId}")
    @ApiOperation(value = "通过userId获取用户信息")
    public BaseResponse getUserInfoByUserId(@PathVariable("userId") Long userId) {
        User user = userService.getById(userId);
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(user,userInfoVo);
        return ResultUtils.success(userInfoVo);
    }
    @ApiOperation(value = "更新用户信息")
    @PostMapping("/update")
    public BaseResponse updateUserInfo(@RequestBody UserUpdateRequest userUpdateRequest, @UserId Long userId) {
        User user = new User();
        user.setId(userId);
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean update = userService.updateById(user);
        if (!update) {
            throw new BusinessException(ErrorCode.UPDATE_ERROR);
        }
        return ResultUtils.success();
    }
}

