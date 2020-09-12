package com.atguigu.gmall.auth.service.impl;

import com.atguigu.gmall.auth.config.JwtProperties;
import com.atguigu.gmall.auth.feign.GmallUmsClient;
import com.atguigu.gmall.auth.service.AuthService;
import com.atguigu.gmall.common.exception.UserException;
import com.atguigu.gmall.common.utils.CookieUtils;
import com.atguigu.gmall.common.utils.IpUtil;
import com.atguigu.gmall.common.utils.JwtUtils;
import com.atguigu.gmall.ums.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月10日 18时34分
 */
@Service
@EnableConfigurationProperties(JwtProperties.class)
public class AuthServiceImpl implements AuthService {
    @Autowired
    private GmallUmsClient gmallUmsClient;

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public void login(String loginName, String password, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 获取用户信息
        UserEntity userEntity = gmallUmsClient.queryUser(loginName, password).getData();
        // 判空
        if (userEntity == null) {
            throw new UserException("用户名或密码不正确！");
        }
        // 将用户id和用户名放入载荷并生成jwt
        HashMap<String, Object> map = new HashMap<>();
        map.put("userId", userEntity.getId());
        map.put("username", userEntity.getUsername());
        // 为了防止jwt被盗取，载荷中加入用户IP地址
        map.put("ip", IpUtil.getIpAddressAtService(request));
        // 制作jwt类型的token
        String jwt = JwtUtils.generateToken(map, jwtProperties.getPrivateKey(), jwtProperties.getExpire());
        // 将jwt放入cookie中
        CookieUtils.setCookie(request, response, jwtProperties.getCookieName(), jwt, jwtProperties.getExpire() * 60);
        // 用户昵称放入cookie，用于用户展示
        CookieUtils.setCookie(request, response, jwtProperties.getUnick(), userEntity.getNickname(), jwtProperties.getExpire() * 60);
    }
}
