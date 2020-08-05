package com.atguigu.gmall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * @author Cerosg
 * @describable 配置跨域解决方案
 * @create 2020年07月21日 17时16分
 */
@Configuration
public class CorsConfig {
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        // 允许的域
        corsConfig.addAllowedOrigin("http://manager.gmall.com");
        corsConfig.addAllowedOrigin("http://www.gmall.com");
        corsConfig.addAllowedOrigin("http://gmall.com");
        // 允许的头信息
        corsConfig.addAllowedHeader("*");
        // 允许的请求方式
        corsConfig.addAllowedMethod("*");
        // 设置允许携带cookie信息
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        corsConfigurationSource.registerCorsConfiguration("/**", corsConfig);
        return new CorsWebFilter(corsConfigurationSource);
    }
}
