package com.atguigu.gmall.pms.service.impl;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.pms.entity.CategoryEntity;
import com.atguigu.gmall.pms.mapper.CategoryMapper;
import com.atguigu.gmall.pms.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, CategoryEntity> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<CategoryEntity> page = this.page(
                paramVo.getPage(),
                new QueryWrapper<>()
        );
        return new PageResultVo(page);
    }

    @Override
    public List<CategoryEntity> querySubMenuByPid(Long pid) {
        return categoryMapper.querySubMenuByPid(pid);
    }

    @Override
    public List<CategoryEntity> queryAllCategoriesById(Long id) {
        CategoryEntity lv3 = categoryMapper.selectById(id);
        CategoryEntity lv2 = categoryMapper.selectById(lv3.getParentId());
        CategoryEntity lv1 = categoryMapper.selectById(lv2.getParentId());
        return Arrays.asList(lv1, lv2, lv3);
    }
}