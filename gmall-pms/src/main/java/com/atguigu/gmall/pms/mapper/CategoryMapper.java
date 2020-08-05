package com.atguigu.gmall.pms.mapper;

import com.atguigu.gmall.pms.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 商品三级分类
 * 
 * @author Cerosg
 * @email Cerosg@outlook.com
 * @date 2020-07-20 19:34:59
 */
@Mapper
public interface CategoryMapper extends BaseMapper<CategoryEntity> {

    List<CategoryEntity> querySubMenuByPid(Long pid);
}
