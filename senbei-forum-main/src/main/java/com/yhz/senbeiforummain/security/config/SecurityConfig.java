package com.yhz.senbeiforummain.security.config;

import com.yhz.senbeiforummain.security.filter.MyOncePerRequestFilter;
import com.yhz.senbeiforummain.security.handler.MyAccessDeniedHandler;
import com.yhz.senbeiforummain.security.handler.MyAuthenticationEntryPoint;
import com.yhz.senbeiforummain.service.impl.AuthUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * @author 吉良吉影
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private MyOncePerRequestFilter myOncePerRequestFilter;
    private final AuthUserDetailsServiceImpl authUserDetailsService;

    @Autowired
    public SecurityConfig(AuthUserDetailsServiceImpl authUserDetailsService) {
        this.authUserDetailsService = authUserDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authUserDetailsService);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Resource
    private MyAuthenticationEntryPoint myAuthenticationEntryPoint;

    @Resource
    private MyAccessDeniedHandler myAccessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //1、关闭csrf，关闭Session
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //2、设置不需要认证的URL
        http
                .authorizeRequests()
                //允许未登录的用户进行访问
                .antMatchers("/user/login", "/user/register/**", "/user/email/**").permitAll()
                //允许访问knife4j
                .antMatchers("/doc.html", "/webjars/**", "/img.icons/**", "/swagger-resources/**", "/**", "/v2/api-docs").permitAll()
                //oauth2
                .antMatchers("/oauth/**").permitAll()
                //其余url都要认证才能访问
                .anyRequest().authenticated();
        //3、在UsernamePasswordAuthenticationFilter前添加认证过滤器
        http.addFilterBefore(myOncePerRequestFilter, UsernamePasswordAuthenticationFilter.class);
        //4、异常处理
        http
                .exceptionHandling()
                //认证失败处理器
                .authenticationEntryPoint(myAuthenticationEntryPoint)
                //权限不足处理器
                .accessDeniedHandler(myAccessDeniedHandler);

        //5,跨域处理
        http
                .cors()
                .configurationSource(configurationSource());
    }

    CorsConfigurationSource configurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("*"));
        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
