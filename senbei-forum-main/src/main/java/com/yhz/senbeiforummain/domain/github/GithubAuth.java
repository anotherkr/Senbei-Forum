package com.yhz.senbeiforummain.domain.github;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * github认证信息
 * @author yanhuanzhan
 * @date 2022/12/16 - 2:33
 */
@Data
@Component
@ConfigurationProperties(prefix = "oauth.github")
public class GithubAuth {
    /**
     * 客户端 id
     */
    private String clientId;

    /**
     * 客户端密钥
     */
    private String clientSecret;

    /**
     * 重定向地址
     */
    private String redirectUri;
    /**
     * 重定向到前端
     */
    private String frontRedirectUrl;

}
