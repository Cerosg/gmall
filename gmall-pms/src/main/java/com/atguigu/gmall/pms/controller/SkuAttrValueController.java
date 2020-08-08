package com.atguigu.gmall.pms.controller;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.pms.entity.SkuAttrValueEntity;
import com.atguigu.gmall.pms.service.SkuAttrValueService;
import com.atguigu.gmall.pms.vo.SaleAttrValueVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * sku销售属性&值
 *
 * @author Cerosg
 * @email Cerosg@outlook.com
 * @date 2020-07-20 19:34:59
 */
@Api(tags = "sku销售属性&值 管理")
@RestController
@RequestMapping("/pms/skuattrvalue")
public class SkuAttrValueController {

    @Autowired
    private SkuAttrValueService skuAttrValueService;

    /**
     * 根据spuId查询spu下所有sku可能组合与skuId之间的映射关系
     */
    @ApiOperation("根据spuId查询spu下所有sku可能组合与skuId之间的映射关系")
    @GetMapping("spu/sku/{spuId}")
    public ResponseVo<String> querySaleAttrMappingBySpuId(@PathVariable("spuId") Long spuId) {
        return ResponseVo.ok(skuAttrValueService.querySaleAttrMappingBySpuId(spuId));
    }

    /**
     * 根据skuId查询sku的销售属性集合
     */
    @ApiOperation("根据skuId查询sku的销售属性集合")
    @GetMapping("sku/{skuId}")
    public ResponseVo<List<SkuAttrValueEntity>> querySaleAttrValueVoBySkuId(@PathVariable("skuId") Long skuId) {
        return ResponseVo.ok(skuAttrValueService.list(new QueryWrapper<SkuAttrValueEntity>().eq("sku_id", skuId)));
    }

    /**
     * 根据skuId查询spu下所有销售属性的可选值
     */
    @ApiOperation("根据spuId查询spu下所有销售属性的可选值")
    @GetMapping("spu/{spuId}")
    public ResponseVo<List<SaleAttrValueVo>> querySaleAttrValueVoBySpuId(@PathVariable("spuId") Long spuId) {
        return ResponseVo.ok(skuAttrValueService.querySaleAttrValueVoBySpuId(spuId));
    }

    /**
     * 根据skuID查询搜索属性值对象
     */
    @ApiOperation("根据skuID查询搜索属性值对象")
    @GetMapping("search/{skuId}")
    public ResponseVo<List<SkuAttrValueEntity>> querySearchAttrValueBySkuId(@PathVariable("skuId") Long skuId) {
        List<SkuAttrValueEntity> skuAttrValueEntities = skuAttrValueService.querySearchAttrValueBySkuId(skuId);
        return ResponseVo.ok(skuAttrValueEntities);
    }

    /**
     * 列表
     */
    @GetMapping
    @ApiOperation("分页查询")
    public ResponseVo<PageResultVo> querySkuAttrValueByPage(PageParamVo paramVo) {
        PageResultVo pageResultVo = skuAttrValueService.queryPage(paramVo);

        return ResponseVo.ok(pageResultVo);
    }


    /**
     * 信息
     */
    @GetMapping("{id}")
    @ApiOperation("详情查询")
    public ResponseVo<SkuAttrValueEntity> querySkuAttrValueById(@PathVariable("id") Long id) {
        SkuAttrValueEntity skuAttrValue = skuAttrValueService.getById(id);

        return ResponseVo.ok(skuAttrValue);
    }

    /**
     * 保存
     */
    @PostMapping
    @ApiOperation("保存")
    public ResponseVo<Object> save(@RequestBody SkuAttrValueEntity skuAttrValue) {
        skuAttrValueService.save(skuAttrValue);

        return ResponseVo.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation("修改")
    public ResponseVo update(@RequestBody SkuAttrValueEntity skuAttrValue) {
        skuAttrValueService.updateById(skuAttrValue);

        return ResponseVo.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除")
    public ResponseVo delete(@RequestBody List<Long> ids) {
        skuAttrValueService.removeByIds(ids);

        return ResponseVo.ok();
    }

}
