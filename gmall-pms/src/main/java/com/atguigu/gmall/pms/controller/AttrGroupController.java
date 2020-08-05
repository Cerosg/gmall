package com.atguigu.gmall.pms.controller;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.pms.entity.AttrGroupEntity;
import com.atguigu.gmall.pms.service.AttrGroupService;
import com.atguigu.gmall.pms.vo.GroupVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 属性分组
 *
 * @author Cerosg
 * @email Cerosg@outlook.com
 * @date 2020-07-20 19:34:59
 */
@Api(tags = "属性分组 管理")
@RestController
@RequestMapping("pms/attrgroup")
public class AttrGroupController {

    @Autowired
    private AttrGroupService attrGroupService;


    /**
     * 属性三级分类分组列表和分组中的属性
     *
     * @param id 三级分类id
     * @return
     */
    @GetMapping("/withattrs/{catId}")
    @ApiOperation("根据分类id查询属性分组和分组中的属性")
    public ResponseVo<List<GroupVo>> queryAttrGroupAndValueByCid(@PathVariable("catId") Long id) {
        return ResponseVo.ok(attrGroupService.queryAttrGroupAndValueByCid(id));
    }

    /**
     * 属性三级分类分组列表
     */
    @GetMapping("/category/{cid}")
    @ApiOperation("根据分类id查询属性分组")
    public ResponseVo<List<AttrGroupEntity>> queryAttrGroupByCid(@PathVariable("cid") Long id) {
        return ResponseVo.ok(attrGroupService.list(new QueryWrapper<AttrGroupEntity>().eq("category_id", id)));
    }

    /**
     * 列表
     */
    @GetMapping
    @ApiOperation("分页查询")
    public ResponseVo<PageResultVo> queryAttrGroupByPage(PageParamVo paramVo) {
        PageResultVo pageResultVo = attrGroupService.queryPage(paramVo);

        return ResponseVo.ok(pageResultVo);
    }


    /**
     * 信息
     */
    @GetMapping("{id}")
    @ApiOperation("详情查询")
    public ResponseVo<AttrGroupEntity> queryAttrGroupById(@PathVariable("id") Long id) {
        AttrGroupEntity attrGroup = attrGroupService.getById(id);

        return ResponseVo.ok(attrGroup);
    }

    /**
     * 保存
     */
    @PostMapping
    @ApiOperation("保存")
    public ResponseVo<Object> save(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.save(attrGroup);

        return ResponseVo.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation("修改")
    public ResponseVo update(@RequestBody AttrGroupEntity attrGroup) {
        attrGroupService.updateById(attrGroup);

        return ResponseVo.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除")
    public ResponseVo delete(@RequestBody List<Long> ids) {
        attrGroupService.removeByIds(ids);

        return ResponseVo.ok();
    }

}
