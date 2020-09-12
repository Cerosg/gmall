package com.atguigu.gmall.ums.api;

import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.ums.entity.UserEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月10日 18时01分
 */
public interface GmallUmsApi {
    /**
     * 查询用户：用户登录
     */
    @GetMapping("ums/user/query")
    @ApiOperation("查询用户")
    ResponseVo<UserEntity> queryUser(@RequestParam("loginName") String loginName, @RequestParam("password") String password);
}
