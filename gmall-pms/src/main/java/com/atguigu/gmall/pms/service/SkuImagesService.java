package com.atguigu.gmall.pms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.pms.entity.SkuImagesEntity;

import java.util.Map;

/**
 * sku图片
 *
 * @author Cerosg
 * @email Cerosg@outlook.com
 * @date 2020-07-20 19:34:59
 */
public interface SkuImagesService extends IService<SkuImagesEntity> {

    PageResultVo queryPage(PageParamVo paramVo);
}

