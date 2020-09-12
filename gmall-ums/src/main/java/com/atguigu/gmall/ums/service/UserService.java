package com.atguigu.gmall.ums.service;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.ums.entity.UserEntity;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 用户表
 *
 * @author Cerosg
 * @email Cerosg@outlook.com
 * @date 2020-07-20 20:25:53
 */
public interface UserService extends IService<UserEntity> {

    PageResultVo queryPage(PageParamVo paramVo);

    Boolean checkParam(String data, Integer type);

    void sendCode(String phone);

    void register(UserEntity user, String code);

    UserEntity queryUser(String loginName, String password);
}

