package com.freenet.openimdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 跨域配置
 */
@Configuration
public class CorsConfig {
    
    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // 允许特定的源
        config.addAllowedOrigin("http://localhost:3003"); // 允许前端域名
        // 如果有多个前端域名，可以继续添加
        // config.addAllowedOrigin("http://example.com");
        // config.addAllowedOrigin("https://example.com");
        
        // 允许跨域的请求头
        config.addAllowedHeader("*");
        
        // 允许跨域的请求方法
        config.addAllowedMethod("*");
        
        // 允许携带cookie等认证信息
        config.setAllowCredentials(true);
        
        // 设置预检请求的有效期，单位为秒
        config.setMaxAge(3600L);
        
        // 对所有接口都生效
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
} 