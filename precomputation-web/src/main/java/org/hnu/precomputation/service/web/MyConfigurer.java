package com.qiehua.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Desc  跨域问题配置类
 * @Version 1.0
 */
@SpringBootConfiguration
public class MyConfigurer implements WebMvcConfigurer {
    /**
     * 所有请求都允许跨域，使用这种配置就不需要在interceptor中配置header了
     */
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry){
        corsRegistry.addMapping("/**")
                .allowCredentials(true)
                .allowedOrigins("http://192.168.70.184:8199")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}

