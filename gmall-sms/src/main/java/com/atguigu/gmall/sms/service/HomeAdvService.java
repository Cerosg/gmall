package com.atguigu.gmall.sms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.sms.entity.HomeAdvEntity;

import java.util.Map;

/**
 * 首页轮播广告
 *
 * @author Cerosg
 * @email Cerosg@outlook.com
 * @date 2020-07-20 20:51:21
 */
public interface HomeAdvService extends IService<HomeAdvEntity> {

    PageResultVo queryPage(PageParamVo paramVo);
}

