package com.yhz.senbeiforummain.handler.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yhz.commonutil.common.ErrorCode;
import com.yhz.senbeiforummain.constant.rediskey.RedisUserKey;
import com.yhz.senbeiforummain.handler.OauthLoginHandler;
import com.yhz.senbeiforummain.model.entity.ThirdUser;
import com.yhz.senbeiforummain.model.entity.User;
import com.yhz.senbeiforummain.config.oauth.GithubAuthConfig;
import com.yhz.senbeiforummain.model.dto.github.GithubTokenRequest;
import com.yhz.senbeiforummain.model.dto.github.GithubUserRequest;
import com.yhz.senbeiforummain.model.enums.LoginChannelEnum;
import com.yhz.senbeiforummain.model.enums.RoleEnum;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.security.domain.AuthUser;
import com.yhz.senbeiforummain.service.IRoleService;
import com.yhz.senbeiforummain.service.IUserService;
import com.yhz.senbeiforummain.service.ThirdUserService;
import com.yhz.senbeiforummain.util.JwtUtil;
import com.yhz.senbeiforummain.util.RedisCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author yanhuanzhan
 * @date 2022/12/16 - 2:17
 */
@Component
@Slf4j
public class GithubLoginHandler implements OauthLoginHandler {
    public static final String TOKEN_BASE_URL = "https://github.com/login/oauth/access_token";
    public static final String AUTHORIZE_BASE_URL = "https://gitee.com/oauth/authorize";
    public static final String USER_INFO_BASE_URL = "https://gitee.com/api/v5/user";
    @Resource
    private RedisCache redisCache;
    @Resource
    private ThirdUserService thirdUserService;
    @Resource
    private IUserService userService;
    @Resource
    private IRoleService roleService;
    @Resource
    private TransactionTemplate transactionTemplate;
    @Resource
    GithubAuthConfig githubAuthConfig;

    @Override
    public LoginChannelEnum getChannel() {
        return LoginChannelEnum.GITHUB_LOGIN;
    }

    @Transactional(rollbackFor = BusinessException.class)
    @Override
    public void dealLogin(String code, HttpServletResponse response) {
        //拼装获取accessToken url
        String accessTokenUrl = TOKEN_BASE_URL
                .concat("?client_id=")
                .concat(githubAuthConfig.getClientId())
                .concat("&client_secret=")
                .concat(githubAuthConfig.getClientSecret())
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
        String body = HttpUtil.createGet("https://api.github.com/user")
                .header("X-GitHub-Api-Version", "2022-11-28")
                .header(HttpHeaders.ACCEPT, "application/vnd.github+json")
                .header(HttpHeaders.AUTHORIZATION, authorization).execute().body();
        GithubUserRequest githubUserRequest = JSONObject.parseObject(body, GithubUserRequest.class);
        String thirdUserAvatarUrl = githubUserRequest.getAvatarUrl();
        String thirdUserNickname = githubUserRequest.getLogin();
        Long thirdUserId = githubUserRequest.getId();
        //第三方用户的用户名和密码使用uuid
        String username = UUID.randomUUID().toString().replace("_", "").substring(20);
        String password = UUID.randomUUID().toString().replace("_", "").substring(10);

        //查看是否已经注册过
        QueryWrapper<ThirdUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("third_id", thirdUserId);
        queryWrapper.eq("channel", getChannel().getCode());
        AtomicReference<ThirdUser> thirdUser = new AtomicReference<>(thirdUserService.getOne(queryWrapper));
        AtomicReference<User> finalUser = new AtomicReference<>();

        if (ObjectUtil.isEmpty(thirdUser.get())) {
            //如果没有注册过，则进行注册
            transactionTemplate.execute(transactionStatus -> {
                //保存本地用户
                finalUser.set(new User());
                boolean res = userService.save(finalUser.get());
                if (!res) {
                    throw new BusinessException(ErrorCode.SAVE_ERROR);
                }
                finalUser.get().setUsername(username)
                        .setNickname("用户"+ finalUser.get().getId())
                        .setHeadUrl(githubUserRequest.getAvatarUrl());

                boolean update = userService.updateById(finalUser.get());
                if (!update) {
                    throw new BusinessException(ErrorCode.UPDATE_ERROR);
                }
                //设置角色
                if (!roleService.attachRoleByUserId(RoleEnum.ROLE_USER.getId(), finalUser.get().getId())) {
                    throw new BusinessException(ErrorCode.SAVE_ERROR);
                }
                //保存第三方用户
                thirdUser.set(new ThirdUser());
                thirdUser.get().setUserId(finalUser.get().getId()).setThirdId(thirdUserId)
                        .setNickname(thirdUserNickname).setChannel(this.getChannel().getCode());
                thirdUserService.save(thirdUser.get());
                return null;
            });

        }
        Long userId = thirdUser.get().getUserId();
        User user = userService.getById(userId);
        AuthUser authUser = new AuthUser();
        authUser.setUser(user);
        String token = JwtUtil.createJWT(username);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username,password);
        //将用户存入上下文中
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        redisCache.setCacheObject(RedisUserKey.getUserToken, username, token);
        redisCache.setCacheObject(RedisUserKey.getUserInfo, username, authUser);

        //重定向到前端
        try {
            response.sendRedirect(githubAuthConfig.getFrontRedirectUrl() + "?token=" + token);
        } catch (IOException e) {
            log.error("Redirect error:{}", e.getMessage());
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
    }

    @Override
    public String getUrl() {
        return "https://github.com/login/oauth/authorize?client_id="
                .concat(githubAuthConfig.getClientId())
                .concat("&redirect_uri=")
                .concat(githubAuthConfig.getRedirectUri());
    }
}
