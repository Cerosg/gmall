package com.atguigu.gmall.pms.api;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.pms.vo.ItemGroupVo;
import com.atguigu.gmall.pms.vo.SaleAttrValueVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Cerosg
 * @describable
 * @create 2020年07月31日 14时30分
 */
public interface GmallPmsApi {
    @ApiOperation("分页查询spu,用于搜索页面")
    @PostMapping("/pms/spu/json")
    ResponseVo<List<SpuEntity>> querySpuByPageJson(@RequestBody PageParamVo paramVo);

    @ApiOperation("根据spuId查询对应的sku")
    @GetMapping("/pms/sku/spu/{spuId}")
    ResponseVo<List<SkuEntity>> querySkuBySpuId(@PathVariable("spuId") Long spuId);

    @ApiOperation("根据brandId查询品牌")
    @GetMapping("/pms/brand/{id}")
    ResponseVo<BrandEntity> queryBrandById(@PathVariable("id") Long id);

    @ApiOperation("根据categoryId查询分类")
    @GetMapping("/pms/category/{id}")
    ResponseVo<CategoryEntity> queryCategoryById(@PathVariable("id") Long id);

    @ApiOperation("根据skuID查询搜索属性值对象")
    @GetMapping("/pms/skuattrvalue/search/{skuId}")
    ResponseVo<List<SkuAttrValueEntity>> querySearchAttrValueBySkuId(@PathVariable("skuId") Long skuId);

    @ApiOperation("根据spuId查询搜索属性值对象")
    @GetMapping("/pms/spuattrvalue/search/{spuId}")
    ResponseVo<List<SpuAttrValueEntity>> querySearchAttrValueBySpuId(@PathVariable("spuId") Long spuId);

    @GetMapping("/pms/spu/{id}")
    @ApiOperation("详情查询")
    ResponseVo<SpuEntity> querySpuById(@PathVariable("id") Long id);

    @GetMapping("/pms/category/parent/{parentId}")
    @ApiOperation("根据父id查询分类")
    ResponseVo<List<CategoryEntity>> queryCategoryByPid(@PathVariable("parentId") Long pid);

    @GetMapping("/pms/category/cates/{pid}")
    @ApiOperation("根据父id查询分类，CategoryEntity中扩展了subs字段")
    ResponseVo<List<CategoryEntity>> querySubMenuByPid(@PathVariable("pid") Long pid);

    @GetMapping("/pms/sku/{id}")
    @ApiOperation("根据skuId查询sku")
    public ResponseVo<SkuEntity> querySkuById(@PathVariable("id") Long id);

    @GetMapping("/pms/category/all/{id}")
    @ApiOperation("根据三级分类id，查询一二三级分类集合")
    ResponseVo<List<CategoryEntity>> queryAllCategoriesById(@PathVariable("id") Long id);

    @GetMapping("/pms/skuimages/sku/{skuId}")
    @ApiOperation("根据skuId查询图片列表")
    ResponseVo<List<SkuImagesEntity>> querySkuImagesBySkuId(@PathVariable("skuId") Long skuId);

    @ApiOperation("根据spuId查询spu下所有销售属性的可选值")
    @GetMapping("/pms/skuattrvalue/spu/{spuId}")
    ResponseVo<List<SaleAttrValueVo>> querySaleAttrValueVoBySpuId(@PathVariable("spuId") Long spuId);

    @ApiOperation("根据skuId查询sku的销售属性集合")
    @GetMapping("/pms/skuattrvalue/sku/{skuId}")
    ResponseVo<List<SkuAttrValueEntity>> querySaleAttrValueVoBySkuId(@PathVariable("skuId") Long skuId);

    @ApiOperation("根据spuId查询spu下所有sku可能组合与skuId之间的映射关系")
    @GetMapping("/pms/skuattrvalue/spu/sku/{spuId}")
    ResponseVo<String> querySaleAttrMappingBySpuId(@PathVariable("spuId") Long spuId);

    @GetMapping("/pms/spudesc/{spuId}")
    @ApiOperation("根据spuId查询spu的描述信息")
    ResponseVo<SpuDescEntity> querySpuDescById(@PathVariable("spuId") Long spuId);

    @GetMapping("/pms/attrgroup/withattr/withvalue/{cid}")
    @ApiOperation("根据分类id结果spuId/skuId查询组及组下规格参数与值")
    ResponseVo<List<ItemGroupVo>> queryGroupAttrValue(@PathVariable("cid") Long cid, @RequestParam("spuId") Long spuId, @RequestParam("skuId") Long skuId);
}
