package com.yhz.senbeiforummain.config;


import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author yanhuanzhan
 * @date 2022/11/8 - 21:29
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
public class Knife4jConfig {
    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        ApiSelectorBuilder builder = new Docket(DocumentationType.SWAGGER_2)
                .enableUrlTemplating(false)
                .apiInfo(apiInfo())
                // 选择那些路径和api会生成document
                .select()
                // 对所有api进行监控
                .apis(RequestHandlerSelectors.basePackage("com.yhz.senbeiforummain.controller"))
                //这里可以自定义过滤
                .paths(this::filterPath);

        return builder.build();
    }

    private boolean filterPath(String path) {
        boolean ret = path.endsWith("/error");
        if (ret) {
            return false;
        }
        //这块可以写其他的过滤逻辑
        return true;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("title")
                .description("仙贝社区接口文档")
                .termsOfServiceUrl("114514")
                .version("1.0")
                .contact(new Contact("吉良吉影", "114514", "1983890621@qq.com"))
                .build();
    }
}
