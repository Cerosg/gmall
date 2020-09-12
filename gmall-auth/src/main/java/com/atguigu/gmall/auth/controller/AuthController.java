package com.atguigu.gmall.auth.controller;

import com.atguigu.gmall.auth.service.AuthService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月10日 18时33分
 */
@Controller
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("login")
    @ApiOperation("用户登录")
    public String login(@RequestParam("loginName") String loginName,
                        @RequestParam("password") String password,
                        @RequestParam(value = "returnUrl", required = false) String returnUrl,
                        HttpServletRequest request, HttpServletResponse response) throws Exception {
        authService.login(loginName, password, request, response);
        if (StringUtils.isNotBlank(returnUrl)) {
            return "redirect:" + returnUrl;
        }
        // 若没有returnUrl，直接跳转到商城首页
        return "redirect:http://www.gmall.com";
    }

    /**
     * 从任意地址跳转到登录页面
     *
     * @param model     视图
     * @param returnUrl 原来的地址
     * @return 登录页面
     */
    @GetMapping("toLogin.html")
    @ApiOperation("跳转到登录页面")
    public String toLogin(Model model, @RequestParam(value = "returnUrl", required = false) String returnUrl) {
        model.addAttribute("returnUrl", returnUrl);
        return "login";
    }
}
