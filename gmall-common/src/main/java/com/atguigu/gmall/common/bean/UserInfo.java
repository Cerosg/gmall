package com.atguigu.gmall.common.bean;

import lombok.Data;

/**
 * 封装用户登录信息
 *
 * @author Cerosg
 * @create 2020年08月11日 13时34分
 */
@Data
public class UserInfo {
    private Long userId;
    private String userKey;
}
