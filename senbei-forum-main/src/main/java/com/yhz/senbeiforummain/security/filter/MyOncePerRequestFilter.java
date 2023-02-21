package com.yhz.senbeiforummain.security.filter;

import cn.hutool.core.util.StrUtil;
import com.yhz.commonutil.common.ErrorCode;
import com.yhz.senbeiforummain.constant.rediskey.RedisUserKey;
import com.yhz.senbeiforummain.exception.BusinessException;
import com.yhz.senbeiforummain.security.domain.AuthUser;
import com.yhz.senbeiforummain.util.JwtUtil;
import com.yhz.senbeiforummain.util.RedisCache;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @Author LZDWTL
 * @Date 2021-12-20 16:28
 * @ClassName ${MyOncePerRequestFilter}
 * @Description ${认证过滤器}
 */
@Component
public class MyOncePerRequestFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(MyOncePerRequestFilter.class);

    @Value("${jwt.tokenHeader}")
    private String header;

    @Resource
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        // header的值是在yml文件中定义的 “Authorization”
        String token = request.getHeader(header);
        logger.info("MyOncePerRequestFilter-token:{}", token);
        if (!StrUtil.isEmpty(token)) {
            String username;
            try {
                Claims claims = JwtUtil.parseJWT(token);
                username = claims.getSubject();
            } catch (Exception e) {
                logger.error("token validate failed,exception:{}", e);
                throw new BusinessException(ErrorCode.TOKEN_VALIDATE_FAILED);
            }
            String redisToken = redisCache.getCacheObject(RedisUserKey.getUserToken, username);
            logger.info("MyOncePerRequestFilter-redisToken:{}", redisToken);
            if (StrUtil.isEmpty(redisToken)) {
                //token令牌验证失败
                throw new BusinessException(ErrorCode.TOKEN_VALIDATE_FAILED);
            }

            //对比前端发送请求携带的的token是否与redis中存储的一致
            if (!Objects.isNull(redisToken) && redisToken.equals(token)) {
                AuthUser authUser = redisCache.getCacheObject(RedisUserKey.getUserInfo,username);
                logger.info("MyOncePerRequestFilter-authUser:{}", authUser);
                if (Objects.isNull(authUser)) {
                    throw new BusinessException(ErrorCode.NOT_LOGIN);
                }
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
}


