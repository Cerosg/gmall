package com.atguigu.gmall.search;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.search.feign.GmallPmsClient;
import com.atguigu.gmall.search.feign.GmallWmsClient;
import com.atguigu.gmall.search.pojo.Goods;
import com.atguigu.gmall.search.pojo.SearchAttrValue;
import com.atguigu.gmall.search.repository.GoodsRepository;
import com.atguigu.gmall.wms.entity.WareSkuEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class GmallSearchApplicationTests {
    @Autowired
    private GoodsRepository goodsRepository;
    @Autowired
    private GmallPmsClient gmallPmsClient;
    @Autowired
    private GmallWmsClient gmallWmsClient;

    @Test
    void contextLoads() {
        int pageNum = 1, pageSize = 100;
        do {
            // 分批查询spu
            PageParamVo pageParamVo = new PageParamVo();
            pageParamVo.setPageNum(pageNum);
            pageParamVo.setPageSize(pageSize);
            List<SpuEntity> spuEntities = gmallPmsClient.querySpuByPageJson(pageParamVo).getData();
            if (CollectionUtils.isEmpty(spuEntities))
                return;
            // 遍历spu集合，查询每一个spu下的所有sku，导入索引库
            spuEntities.forEach(spuEntity -> {
                List<SkuEntity> skuEntities = gmallPmsClient.querySkuBySpuId(spuEntity.getId()).getData();
                if (!CollectionUtils.isEmpty(skuEntities)) {
                    List<Goods> goodsList = skuEntities.stream().map(skuEntity -> {
                        Goods goods = new Goods();
                        Long skuId = skuEntity.getId();
                        // sku相关属性值设置
                        goods.setSkuId(skuId);
                        goods.setImage(skuEntity.getDefaultImage());
                        goods.setPrice(skuEntity.getPrice().doubleValue());
                        goods.setTitle(skuEntity.getTitle());
                        goods.setSubTitle(skuEntity.getSubtitle());
                        // spu相关属性设置
                        goods.setCreateTime(spuEntity.getCreateTime());
                        // brand相关属性设置
                        Long brandId = skuEntity.getBrandId();
                        goods.setBrandId(brandId);
                        BrandEntity brandEntity = gmallPmsClient.queryBrandById(brandId).getData();
                        if (brandEntity != null) {
                            goods.setBrandName(brandEntity.getName());
                            goods.setLogo(brandEntity.getLogo());
                        }
                        // wms库存相关属性设置
                        List<WareSkuEntity> wareSkuEntities = gmallWmsClient.queryWareSkuBySkuId(skuId).getData();
                        if (!CollectionUtils.isEmpty(wareSkuEntities)) {
                            goods.setStock(wareSkuEntities
                                    .stream()
                                    .anyMatch(wareSkuEntity -> wareSkuEntity.getStock() - wareSkuEntity.getStockLocked() > 0));
                            goods.setSales(wareSkuEntities
                                    .stream()
                                    .map(WareSkuEntity::getSales)
                                    .reduce(Long::sum)
                                    .get());
                        }
                        // category相关属性设置
                        Long categoryId = skuEntity.getCatagoryId();
                        CategoryEntity categoryEntity = gmallPmsClient.queryCategoryById(categoryId).getData();
                        if (categoryEntity != null) {
                            goods.setCategoryId(categoryId);
                            goods.setCategoryName(categoryEntity.getName());
                        }
                        // searchAttrs相关属性设置
                        List<SearchAttrValue> searchAttrValues = new ArrayList<>();
                        List<SpuAttrValueEntity> spuAttrValueEntities = gmallPmsClient.querySearchAttrValueBySpuId(spuEntity.getId()).getData();
                        if (!CollectionUtils.isEmpty(spuAttrValueEntities)) {
                            searchAttrValues.addAll(spuAttrValueEntities
                                    .stream()
                                    .map(spuAttrValueEntity -> {
                                        SearchAttrValue searchAttrValue = new SearchAttrValue();
                                        searchAttrValue.setAttrId(spuAttrValueEntity.getAttrId());
                                        searchAttrValue.setAttrName(spuAttrValueEntity.getAttrName());
                                        searchAttrValue.setAttrValue(spuAttrValueEntity.getAttrValue());
                                        return searchAttrValue;
                                    }).collect(Collectors.toList()));
                        }
                        List<SkuAttrValueEntity> skuAttrValueEntities = gmallPmsClient.querySearchAttrValueBySkuId(skuId).getData();
                        if (!CollectionUtils.isEmpty(skuAttrValueEntities)) {
                            searchAttrValues.addAll(skuAttrValueEntities
                                    .stream()
                                    .map(skuAttrValueEntity -> {
                                        SearchAttrValue searchAttrValue = new SearchAttrValue();
                                        searchAttrValue.setAttrId(skuAttrValueEntity.getAttrId());
                                        searchAttrValue.setAttrName(skuAttrValueEntity.getAttrName());
                                        searchAttrValue.setAttrValue(skuAttrValueEntity.getAttrValue());
                                        return searchAttrValue;
                                    }).collect(Collectors.toList()));
                        }
                        goods.setSearchAttrs(searchAttrValues);
                        return goods;
                    }).collect(Collectors.toList());
                    goodsRepository.saveAll(goodsList);
                }
            });
            pageSize = spuEntities.size();
            pageNum++;
        } while (pageSize == 100);
    }

}
