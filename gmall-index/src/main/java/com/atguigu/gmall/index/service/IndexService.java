package com.atguigu.gmall.index.service;

import com.atguigu.gmall.pms.entity.CategoryEntity;

import java.util.List;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月05日 09时50分
 */
public interface IndexService {
    /**
     * 查询一级分类
     *
     * @return 一级分类列表
     */
    List<CategoryEntity> getCategoryLv1ById();

    /**
     * 根据父级分类id，查询子级分类
     *
     * @param pid 父级分类id
     * @return 对应子级分类列表
     */
    List<CategoryEntity> querySubMenuByPid(Long pid);
}
