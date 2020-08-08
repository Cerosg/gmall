package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.pms.entity.CategoryEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 商品三级分类
 *
 * @author Cerosg
 * @email Cerosg@outlook.com
 * @date 2020-07-20 19:34:59
 */
public interface CategoryService extends IService<CategoryEntity> {
    PageResultVo queryPage(PageParamVo paramVo);

    List<CategoryEntity> querySubMenuByPid(Long pid);

    List<CategoryEntity> queryAllCategoriesById(Long id);


}

