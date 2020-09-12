package com.atguigu.gmall.ums.service.impl;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.ums.entity.UserEntity;
import com.atguigu.gmall.ums.mapper.UserMapper;
import com.atguigu.gmall.ums.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;


@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<UserEntity> page = this.page(
                paramVo.getPage(),
                new QueryWrapper<UserEntity>()
        );

        return new PageResultVo(page);
    }

    @Override
    public Boolean checkParam(String data, Integer type) {
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        // 1，用户名；2，手机；3，邮箱
        switch (type) {
            case 1:
                wrapper.eq("username", data);
                break;
            case 2:
                wrapper.eq("phone", data);
                break;
            case 3:
                wrapper.eq("email", data);
                break;
            default:
                return null;
        }
        return userMapper.selectCount(wrapper) == 0;
    }

    @Override
    public void sendCode(String phone) {
        // TODO: 2020/8/10 注册用户，利用阿里云发送短信验证码。
    }

    @Override
    public void register(UserEntity user, String code) {
        // 1. 校验短信验证码
        // TODO: 2020/8/10  校验短信验证码
        // 2. 生成盐
        String salt = UUID.randomUUID().toString().substring(0, 7);
        user.setSalt(salt);
        // 3. 对密码加密
        user.setPassword(DigestUtils.md5Hex(user.getPassword() + salt));
        // 4. 写入数据库
        user.setLevelId(1L);// 初始化用户等级
        user.setCreateTime(new Date());// 初始化注册时间
        user.setSourceType(1);// 初始化来源
        user.setIntegration(1000);// 初始化购物积分
        user.setGrowth(1000);// 初始化赠送积分
        user.setStatus(1);// 初始化状态
        save(user);
        // 5. 删除redis中的短信验证码
        // TODO: 2020/8/10 删除短信验证码
    }

    @Override
    public UserEntity queryUser(String loginName, String password) {
        // 根据用户登录名查询用户，可能的登录名有username、phone和email
        UserEntity userEntity = getOne(new QueryWrapper<UserEntity>().eq("username", loginName).or().eq("phone", loginName).or().eq("email", loginName));
        // 判空
        if (userEntity == null) {
            // 用户名不合法
            return null;
        }
        // 获取盐，并对用户输入的明文密码加盐
        password = DigestUtils.md5Hex(password + userEntity.getSalt());
        // 比较密码
        if (!StringUtils.equals(password, userEntity.getPassword())) {
            // 密码不合法
            return null;
        }
        // 返回用户信息
        return userEntity;
    }
}