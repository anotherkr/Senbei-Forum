package com.yhz.senbeiforummain.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.messaging.converter.GsonMessageConverter;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author yanhuanzhan
 * @date 2023/3/2 - 13:02
 */
public class GsonConfig {
    @Value("${spring.gson.date-format}")
    private String dateFormat;

    /**
     * 将解析器注入spring容器，替代默认的解析器
     * @return
     */
    @Bean
    public HttpMessageConverters httpMessageConverters() {
        Collection<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
        gsonHttpMessageConverter.setGson(new GsonBuilder().setDateFormat(dateFormat).create());
        messageConverters.add(gsonHttpMessageConverter);
        return new HttpMessageConverters(true, messageConverters);
    }
}
