package com.atguigu.gmall.cart.interceptor;

import com.atguigu.gmall.cart.config.JwtProperties;
import com.atguigu.gmall.common.bean.UserInfo;
import com.atguigu.gmall.common.utils.CookieUtils;
import com.atguigu.gmall.common.utils.JwtUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

/**
 * 自定义拦截器，用于访问购物车时校验用户登录状态
 *
 * @author Cerosg
 * @create 2020年08月11日 11时44分
 */
@Component
@EnableConfigurationProperties(JwtProperties.class)
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private JwtProperties jwtProperties;
    private static final ThreadLocal<UserInfo> THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 在handler执行之前执行
     *
     * @param request  请求
     * @param response 响应
     * @param handler
     * @return 返回false：不放行；返回true：放行
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userKey = CookieUtils.getCookieValue(request, jwtProperties.getUserKeyName());
        // 如果没有userKey，随机生成一个并放入cookie中
        if (StringUtils.isBlank(userKey)) {
            userKey = UUID.randomUUID().toString();
            CookieUtils.setCookie(request, response, jwtProperties.getUserKeyName(), userKey, 180 * 24 * 3600);
        }
        String token = CookieUtils.getCookieValue(request, jwtProperties.getCookieName());
        Long userId = null;
        if (StringUtils.isNotBlank(token)) {
            Map<String, Object> infoFromToken = JwtUtils.getInfoFromToken(token, jwtProperties.getPublicKey());
            userId = new Long(infoFromToken.get("userId").toString());
        }
        // 将登录信息传递给后续业务
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setUserKey(userKey);
        THREAD_LOCAL.set(userInfo);
        // 无论如何，都要放行，因此返回true
        return true;
    }

    /**
     * 封装了一个获取线程局部变量值的静态方法
     *
     * @return
     */
    public static UserInfo getUserInfo() {
        return THREAD_LOCAL.get();
    }

    /**
     * 在视图渲染完成之后执行，常在该方法中释放资源
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 必须手动释放线程局部变量，否则可能造成内存泄露
        THREAD_LOCAL.remove();
    }
}
