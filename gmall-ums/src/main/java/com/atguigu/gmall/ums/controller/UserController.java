package com.atguigu.gmall.ums.controller;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.ums.entity.UserEntity;
import com.atguigu.gmall.ums.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户表
 *
 * @author Cerosg
 * @email Cerosg@outlook.com
 * @date 2020-07-20 20:25:53
 */
@Api(tags = "用户表 管理")
@RestController
@RequestMapping("ums/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 查询用户：用户登录
     */
    @GetMapping("/query")
    @ApiOperation("查询用户")
    public ResponseVo<UserEntity> queryUser(@RequestParam("loginName") String loginName, @RequestParam("password") String password) {
        return ResponseVo.ok(userService.queryUser(loginName, password));
    }

    /**
     * 注册用户
     */
    @PostMapping("/register")
    @ApiOperation("注册用户")
    public ResponseVo<Object> register(UserEntity user, @RequestParam("code") String code) {
        userService.register(user, code);
        return ResponseVo.ok();
    }

    /**
     * 发送短信
     */
    @PostMapping("/code")
    @ApiOperation("用阿里云短信服务发送短信验证码")
    public ResponseVo<Object> sendCode(@RequestBody String phone) {
        userService.sendCode(phone);
        return ResponseVo.ok();
    }

    /**
     * 数据校验
     */
    @GetMapping("/check/{data}/{type}")
    @ApiOperation("校验用户名、邮箱或手机号是否已被占用")
    public ResponseVo<Boolean> checkParam(@PathVariable("data") String data, @PathVariable("type") Integer type) {
        return ResponseVo.ok(userService.checkParam(data, type));
    }

    /**
     * 列表
     */
    @GetMapping
    @ApiOperation("分页查询")
    public ResponseVo<PageResultVo> queryUserByPage(PageParamVo paramVo) {
        PageResultVo pageResultVo = userService.queryPage(paramVo);

        return ResponseVo.ok(pageResultVo);
    }


    /**
     * 信息
     */
    @GetMapping("{id}")
    @ApiOperation("详情查询")
    public ResponseVo<UserEntity> queryUserById(@PathVariable("id") Long id) {
        UserEntity user = userService.getById(id);

        return ResponseVo.ok(user);
    }

    /**
     * 保存
     */
    @PostMapping
    @ApiOperation("保存")
    public ResponseVo<Object> save(@RequestBody UserEntity user) {
        userService.save(user);

        return ResponseVo.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation("修改")
    public ResponseVo update(@RequestBody UserEntity user) {
        userService.updateById(user);

        return ResponseVo.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除")
    public ResponseVo delete(@RequestBody List<Long> ids) {
        userService.removeByIds(ids);

        return ResponseVo.ok();
    }

}
