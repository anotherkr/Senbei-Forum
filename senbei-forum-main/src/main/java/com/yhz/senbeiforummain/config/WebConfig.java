package com.yhz.senbeiforummain.config;

import com.yhz.senbeiforummain.common.annotation.resolver.UserIdResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author yanhuanzhan
 * @date 2023/2/28 - 18:01
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 将解析器注入到SpirngMVC
     * @param resolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new UserIdResolver());
    }
}
