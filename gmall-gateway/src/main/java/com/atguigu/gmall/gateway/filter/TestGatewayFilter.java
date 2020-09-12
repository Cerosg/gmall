package com.atguigu.gmall.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月10日 20时00分
 */
@Component
public class TestGatewayFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("全局拦截器，拦截所有经过网关的请求~");
        return chain.filter(exchange);// 放行
    }

    /**
     * 控制全局过滤器的执行顺序
     *
     * @return 执行优先级，小者优先
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
