package com.atguigu.gmall.pms.controller;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.pms.entity.CategoryEntity;
import com.atguigu.gmall.pms.service.CategoryService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品三级分类
 *
 * @author Cerosg
 * @email Cerosg@outlook.com
 * @date 2020-07-20 19:34:59
 */
@Api(tags = "商品三级分类 管理")
@RestController
@RequestMapping("pms/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 根据三级分类id，查询一二三级分类集合
     *
     * @param id 三级分类id
     * @return 对应子级分类列表
     */
    @GetMapping("/all/{id}")
    @ApiOperation("根据三级分类id，查询一二三级分类集合")
    public ResponseVo<List<CategoryEntity>> queryAllCategoriesById(@PathVariable("id") Long id) {
        return ResponseVo.ok(categoryService.queryAllCategoriesById(id));
    }

    /**
     * 根据父级分类id，查询子级分类
     *
     * @param pid 父级分类id
     * @return 对应子级分类列表
     */
    @GetMapping("/cates/{pid}")
    @ApiOperation("根据父id查询分类，CategoryEntity中扩展了subs字段")
    public ResponseVo<List<CategoryEntity>> querySubMenuByPid(@PathVariable("pid") Long pid) {
        return ResponseVo.ok(categoryService.querySubMenuByPid(pid));
    }

    /**
     * 商品分类：根据父id查询分类
     */
    @GetMapping("/parent/{parentId}")
    @ApiOperation("根据父id查询分类")
    public ResponseVo<List<CategoryEntity>> queryCategoryByPid(@PathVariable("parentId") Long pid) {
        QueryWrapper<CategoryEntity> wapper = new QueryWrapper<>();
        if (pid > -1)
            wapper.eq("parent_id", pid);
        return ResponseVo.ok(categoryService.list(wapper));
    }

    /**
     * 列表
     */
    @GetMapping
    @ApiOperation("分页查询")
    public ResponseVo<PageResultVo> queryCategoryByPage(PageParamVo paramVo) {
        PageResultVo pageResultVo = categoryService.queryPage(paramVo);
        return ResponseVo.ok(pageResultVo);
    }

    /**
     * 信息
     */
    @GetMapping("{id}")
    @ApiOperation("详情查询")
    public ResponseVo<CategoryEntity> queryCategoryById(@PathVariable("id") Long id) {
        CategoryEntity category = categoryService.getById(id);
        return ResponseVo.ok(category);
    }

    /**
     * 保存
     */
    @PostMapping
    @ApiOperation("保存")
    public ResponseVo<Object> save(@RequestBody CategoryEntity category) {
        categoryService.save(category);
        return ResponseVo.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation("修改")
    public ResponseVo update(@RequestBody CategoryEntity category) {
        categoryService.updateById(category);

        return ResponseVo.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除")
    public ResponseVo delete(@RequestBody List<Long> ids) {
        categoryService.removeByIds(ids);

        return ResponseVo.ok();
    }

}
