package com.atguigu.gmall.gateway.filter;

import com.atguigu.gmall.common.utils.IpUtil;
import com.atguigu.gmall.common.utils.JwtUtils;
import com.atguigu.gmall.gateway.config.JwtProperties;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月10日 20时04分
 */
@Component
@EnableConfigurationProperties(JwtProperties.class)
public class AuthGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthGatewayFilterFactory.PathsConfig> {
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 通过构造方法指定接收参数的对象类型
     */
    public AuthGatewayFilterFactory() {
        super(PathsConfig.class);
    }

    /**
     * 拦截的业务逻辑
     *
     * @param config
     * @return
     */
    @Override
    public GatewayFilter apply(PathsConfig config) {
        return (exchange, chain) -> {
            // 1.判断当前路径是否在拦截路径中
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            String curPath = request.getURI().getPath();
            List<String> paths = config.paths;
            // 如果当前路径不在黑名单中，直接放行
            if (paths.stream().noneMatch(curPath::contains)) {
                return chain.filter(exchange);
            }
            // 2.获取cookie中的token信息
            String token = request.getHeaders().getFirst("token");
            if (StringUtils.isBlank(token)) {
                MultiValueMap<String, HttpCookie> cookies = request.getCookies();
                String cookieName = jwtProperties.getCookieName();
                if (!CollectionUtils.isEmpty(cookies) && cookies.containsKey(cookieName)) {
                    token = cookies.getFirst(cookieName).getValue();
                }
            }
            // 3.判断token是否为空，拦截（重定向到登录页面）
            if (StringUtils.isBlank(token)) {
                return interceptor(request, response);
            }
            // 4.解析token
            try {
                Map<String, Object> infoFromToken = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
                // 5.防盗用
                String ip = infoFromToken.get("ip").toString();
                String curIp = IpUtil.getIpAddressAtGateway(request);
                if (!StringUtils.equals(ip, curIp)) {
                    // 不一致，直接拦截
                    return interceptor(request, response);
                }
                // 6.将解析后的用户信息传递给后续的服务
                String userId = infoFromToken.get("userId").toString();
                String username = infoFromToken.get("username").toString();
                request = request.mutate().header("userId", userId).header("username", username).build();
                exchange = exchange.mutate().request(request).build();
            } catch (Exception e) {
                e.printStackTrace();
                return interceptor(request, response);
            }
//            System.out.println("自定义过滤器" + paths);
            // 7.放行
            return chain.filter(exchange);
        };
    }

    private Mono<Void> interceptor(ServerHttpRequest request, ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.SEE_OTHER);
        // 重定向到登录页面
        response.getHeaders().set(HttpHeaders.LOCATION, "http://sso.gmall.com/toLogin.html?returnUrl=" + request.getURI());
        // 请求结束返回Mono<Void>
        return response.setComplete();
    }

    @Override
    public ShortcutType shortcutType() {
        return ShortcutType.GATHER_LIST;
    }

    /**
     * 指定配置文件中字段的顺序
     *
     * @return
     */
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("paths");
    }

    /**
     * 内部类：用于接收配置文件中的字段
     */
    @Data
    @ToString
    public static class PathsConfig {
        private List<String> paths;//需要拦截的路径集合
    }
}
