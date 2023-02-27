package com.yhz.senbeiforummain.controller;

import com.yhz.commonutil.common.BaseResponse;
import com.yhz.commonutil.common.ResultUtils;
import com.yhz.senbeiforummain.model.enums.LoginChannelEnum;
import com.yhz.senbeiforummain.service.ILoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * oauth认证控制器
 *
 * @author yanhuanzhan
 * @date 2022/12/16 - 3:03
 */
@RestController
@RequestMapping("/oauth")
@Api(tags = "oauth2.0 认证")
public class OauthController {
    @Resource
    private ILoginService loginService;

    @ApiOperation("github获取跳转url接口")
    @GetMapping("/url/github")
    public BaseResponse<String> getGithubUrl() {
        String url = loginService.getOauthUrl(LoginChannelEnum.GITHUB_LOGIN);
        return ResultUtils.success(url);
    }
    @ApiOperation("gitee获取跳转url接口")
    @GetMapping("/url/gitee")
    public BaseResponse<String> getGiteeUrl() {
        String url = loginService.getOauthUrl(LoginChannelEnum.GITEE_LOGIN);
        return ResultUtils.success(url);
    }
    @ApiOperation("Github重定向接收授权码接口")
    @GetMapping("/github/redirect")
    public void githubRedirect(String code, HttpServletResponse response) throws IOException {
        loginService.oauthLogin(LoginChannelEnum.GITHUB_LOGIN, code, response);
    }
    @ApiOperation("Gitee重定向接收授权码接口")
    @GetMapping("/gitee/redirect")
    public void giteeRedirect(String code, HttpServletResponse response) throws IOException {
        loginService.oauthLogin(LoginChannelEnum.GITEE_LOGIN, code, response);
    }
}
