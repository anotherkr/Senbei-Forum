package com.yhz.senbeiforummain.handler;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.yhz.commonutil.common.ErrorCode;
import com.yhz.senbeiforummain.constant.rediskey.RedisUserKey;
import com.yhz.senbeiforummain.model.entity.User;
import com.yhz.senbeiforummain.model.dto.github.GithubAuthRequest;
import com.yhz.senbeiforummain.model.dto.github.GithubTokenRequest;
import com.yhz.senbeiforummain.model.dto.github.GithubUserRequest;
import com.yhz.senbeiforummain.model.enums.LoginChannelEnum;
import com.yhz.senbeiforummain.model.enums.RoleEnum;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.service.IRoleService;
import com.yhz.senbeiforummain.service.IUserService;
import com.yhz.senbeiforummain.util.JwtUtil;
import com.yhz.senbeiforummain.util.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yanhuanzhan
 * @date 2022/12/16 - 2:17
 */
@Component
@Slf4j
public class GithubLoginHandler implements OauthLoginHandler {
    @Resource
    private RedisCache redisCache;
    @Resource
    private IUserService userService;
    @Resource
    private IRoleService roleService;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    GithubAuthRequest githubAuthRequest;

    @Override
    public LoginChannelEnum getChannel() {
        return LoginChannelEnum.GITHUB_LOGIN;
    }

    @Transactional(rollbackFor = BusinessException.class)
    @Override
    public void dealLogin(String code, HttpServletResponse response) {
        //拼装获取accessToken url
        String accessTokenUrl = "https://github.com/login/oauth/access_token?client_id="
                .concat(githubAuthRequest.getClientId())
                .concat("&client_secret=")
                .concat(githubAuthRequest.getClientSecret())
                .concat("&code=")
                .concat(code);
        HttpRequest httpRequest = HttpUtil.createGet(accessTokenUrl);
        httpRequest.header("Accept", "application/json").timeout(-1);
        //发送请求，拿到accessToken
        String result = httpRequest.execute().body();
        GithubTokenRequest githubTokenRequest = JSONObject.parseObject(result, GithubTokenRequest.class);
        // 获取 github 用户信息
        String authorization = String.join(
                StringUtils.SPACE, githubTokenRequest.getTokenType(), githubTokenRequest.getAccessToken());
        log.info("authorization:{}", authorization);
        String body = HttpUtil.createGet("https://api.github.com/user")
                .header("X-GitHub-Api-Version", "2022-11-28")
                .header(HttpHeaders.ACCEPT, "application/vnd.github+json")
                .header(HttpHeaders.AUTHORIZATION, authorization).execute().body();
        GithubUserRequest githubUserRequest = JSONObject.parseObject(body, GithubUserRequest.class);
        String username = this.getChannel().getValue() + "_" + githubUserRequest.getId();
        //查看是否已经注册过
        User user = userService.getUserByUserName(username);
        if (ObjectUtil.isEmpty(user)) {
            //如果没有注册过，则进行注册
            User finalUser = new User();
            finalUser.setUsername(username)
                    .setNickname(this.getChannel().getValue() + "_" + githubUserRequest.getLogin())
                    .setHeadUrl(githubUserRequest.getAvatarUrl());
            transactionTemplate.execute(transactionStatus -> {
                boolean save = userService.save(finalUser);
                if (!save) {
                    throw new BusinessException(ErrorCode.SAVE_ERROR);
                }
                //设置角色
                if (!roleService.attachRoleByUserId(RoleEnum.ROLE_USER.getId(), finalUser.getId())) {
                    throw new BusinessException(ErrorCode.SAVE_ERROR);
                }
                return null;
            });
            user = finalUser;
        }
        String token = JwtUtil.createJWT(username);
        redisCache.setCacheObject(RedisUserKey.getUserToken,username,token);
        redisCache.setCacheObject(RedisUserKey.getUserInfo,username,user);
        //重定向到前端
        try {
            response.sendRedirect(githubAuthRequest.getFrontRedirectUrl() + "?token=" + token);
        } catch (IOException e) {
            log.error("Redirect error:{}",e.getMessage());
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
    }

    @Override
    public String getUrl() {
        return "https://github.com/login/oauth/authorize?client_id="
                .concat(githubAuthRequest.getClientId())
                .concat("&redirect_uri=")
                .concat(githubAuthRequest.getRedirectUri());
    }
}
