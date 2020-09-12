package com.atguigu.gmall.auth.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月10日 18时34分
 */
public interface AuthService {
    void login(String loginName, String password, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
