package com.atguigu.gmall.item.service.impl;

import com.atguigu.gmall.item.feign.GmallPmsClient;
import com.atguigu.gmall.item.feign.GmallSmsClient;
import com.atguigu.gmall.item.feign.GmallWmsClient;
import com.atguigu.gmall.item.pojo.ItemVo;
import com.atguigu.gmall.item.service.ItemService;
import com.atguigu.gmall.pms.entity.SkuAttrValueEntity;
import com.atguigu.gmall.pms.entity.SkuEntity;
import com.atguigu.gmall.pms.entity.SpuDescEntity;
import com.atguigu.gmall.wms.entity.WareSkuEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月07日 20时42分
 */
@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private GmallPmsClient pmsClient;
    @Autowired
    private GmallSmsClient smsClient;
    @Autowired
    private GmallWmsClient wmsClient;
    @Autowired
    private ThreadPoolExecutor threadPoolExecutor;

    @Override
    public ItemVo loadData(Long skuId) {
        ItemVo itemVo = new ItemVo();
        CompletableFuture<SkuEntity> skuCompletableFuture = CompletableFuture.supplyAsync(() -> {
            SkuEntity skuEntity = pmsClient.querySkuById(skuId).getData();
            if (skuEntity == null) {
                return null;
            }
            // ---------- sku相关属性 ----------
            itemVo.setSkuId(skuId);
            itemVo.setDefaultImage(skuEntity.getDefaultImage());
            itemVo.setTitle(skuEntity.getTitle());
            itemVo.setSubTitle(skuEntity.getSubtitle());
            itemVo.setPrice(skuEntity.getPrice());
            itemVo.setWeight(skuEntity.getWeight());
            return skuEntity;
        }, threadPoolExecutor);
        CompletableFuture<Void> categoryCompletableFuture = skuCompletableFuture.thenAcceptAsync(skuEntity -> {
            // 一二三级分类
            itemVo.setCategories(pmsClient.queryAllCategoriesById(skuEntity.getCatagoryId()).getData());
        }, threadPoolExecutor);
        CompletableFuture<Void> brandCompletableFuture = skuCompletableFuture.thenAcceptAsync(skuEntity -> {
            // 品牌
            Long brandId = skuEntity.getBrandId();
            itemVo.setBrandId(brandId);
            itemVo.setBrandName(pmsClient.queryBrandById(brandId).getData().getName());
        }, threadPoolExecutor);
        CompletableFuture<Void> spuCompletableFuture = skuCompletableFuture.thenAcceptAsync(skuEntity -> {
            // spu的id和名称
            Long spuId = skuEntity.getSpuId();
            itemVo.setSpuId(spuId);
            itemVo.setSpuName(pmsClient.querySpuById(spuId).getData().getName());
        }, threadPoolExecutor);
        // 根据skuId查询sku的图片列表
        CompletableFuture<Void> imageCompletableFuture = CompletableFuture.runAsync(() -> itemVo.setImages(pmsClient.querySkuImagesBySkuId(skuId).getData()), threadPoolExecutor);

        // ---------- sale相关属性 ----------
        // 根据skuId查询sku的营销信息
        CompletableFuture<Void> saleCompletableFuture = CompletableFuture.runAsync(() -> itemVo.setSales(smsClient.querySalesBySkuId(skuId).getData()), threadPoolExecutor);
        // 根据skuId查询sku的库存信息
        CompletableFuture<Void> stockCompletableFuture = CompletableFuture.runAsync(() -> {
            List<WareSkuEntity> wareSkuEntities = wmsClient.queryWareSkuBySkuId(skuId).getData();
            if (!CollectionUtils.isEmpty(wareSkuEntities)) {
                itemVo.setStock(wareSkuEntities.stream().anyMatch(wareSkuEntity -> wareSkuEntity.getStock() - wareSkuEntity.getStockLocked() > 0));
            }
        }, threadPoolExecutor);
        CompletableFuture<Void> saleAttrCompletableFuture = skuCompletableFuture.thenAcceptAsync(skuEntity -> {
            // 根据sku中的spuId查询spu下所有sku的选择值集合
            itemVo.setSaleAttrs(pmsClient.querySaleAttrValueVoBySpuId(skuEntity.getSpuId()).getData());
        }, threadPoolExecutor);
        CompletableFuture<Void> skuSaleCompletableFuture = CompletableFuture.runAsync(() -> {
            // 根据skuId查询当前sku的销售属性
            List<SkuAttrValueEntity> skuAttrValueEntities = pmsClient.querySaleAttrValueVoBySkuId(skuId).getData();
            if (!CollectionUtils.isEmpty(skuAttrValueEntities)) {
                Map<Long, String> saleAttr = skuAttrValueEntities.stream().collect(Collectors.toMap(SkuAttrValueEntity::getAttrId, SkuAttrValueEntity::getAttrValue));
                itemVo.setSaleAttr(saleAttr);
            }
        }, threadPoolExecutor);
        CompletableFuture<Void> mappingCompletableFuture = skuCompletableFuture.thenAcceptAsync(skuEntity -> {
            // 根据sku中spuId查询spu下所有sku销售属性组合与skuId的映射关系
            itemVo.setSkusJson(pmsClient.querySaleAttrMappingBySpuId(skuEntity.getSpuId()).getData());
        }, threadPoolExecutor);
        CompletableFuture<Void> spuImageCompletableFuture = skuCompletableFuture.thenAcceptAsync(skuEntity -> {
            // ---------- 商品描述（图片列表） ----------
            SpuDescEntity spuDescEntity = pmsClient.querySpuDescById(skuEntity.getSpuId()).getData();
            if (spuDescEntity != null) {
                itemVo.setSpuImages(Arrays.asList(StringUtils.split(spuDescEntity.getDecript(), ",")));
            }
        }, threadPoolExecutor);
        CompletableFuture<Void> groupCompletableFuture = skuCompletableFuture.thenAcceptAsync(skuEntity -> {
            // ---------- 规格与包装 ----------
            itemVo.setGroups(pmsClient.queryGroupAttrValue(skuEntity.getCatagoryId(), skuEntity.getSpuId(), skuId).getData());
        }, threadPoolExecutor);
        // 上面的所有方法都完成之后，才能开始新任务
        CompletableFuture.allOf(
                categoryCompletableFuture, brandCompletableFuture, spuCompletableFuture,
                imageCompletableFuture, saleCompletableFuture, stockCompletableFuture, saleAttrCompletableFuture,
                skuSaleCompletableFuture, mappingCompletableFuture, spuImageCompletableFuture, groupCompletableFuture
        ).join();
        return itemVo;
        /*
        =========================================== 使用同步方式远程调用实现功能<开始> ===========================================
        SkuEntity skuEntity = pmsClient.querySkuById(skuId).getData();
        if (skuEntity == null) {
            return null;
        }
        ItemVo itemVo = new ItemVo();
        // 一二三级分类
        Long cid = skuEntity.getCatagoryId();
        itemVo.setCategories(pmsClient.queryAllCategoriesById(cid).getData());

        // 品牌
        Long brandId = skuEntity.getBrandId();
        itemVo.setBrandId(brandId);
        itemVo.setBrandName(pmsClient.queryBrandById(brandId).getData().getName());
        // spu名称
        Long spuId = skuEntity.getSpuId();
        itemVo.setSpuId(spuId);
        itemVo.setSpuName(pmsClient.querySpuById(spuId).getData().getName());

        // ---------- sku相关属性 ----------
        itemVo.setSkuId(skuId);
        itemVo.setDefaultImage(skuEntity.getDefaultImage());
        itemVo.setTitle(skuEntity.getTitle());
        itemVo.setSubtitle(skuEntity.getSubtitle());
        itemVo.setPrice(skuEntity.getPrice());
        itemVo.setWeight(skuEntity.getWeight());
        itemVo.setImages(pmsClient.querySkuImagesBySkuId(skuId).getData());

        // ---------- sale相关属性 ----------
        itemVo.setSales(smsClient.querySalesBySkuId(skuId).getData());
        List<WareSkuEntity> wareSkuEntities = wmsClient.queryWareSkuBySkuId(skuId).getData();
        if (!CollectionUtils.isEmpty(wareSkuEntities)) {
            itemVo.setStock(wareSkuEntities.stream().anyMatch(wareSkuEntity -> wareSkuEntity.getStock() - wareSkuEntity.getStockLocked() > 0));
        }
        itemVo.setSaleAttrs(pmsClient.querySaleAttrValueVoBySpuId(spuId).getData());
        List<SkuAttrValueEntity> skuAttrValueEntities = pmsClient.querySaleAttrValueVoBySkuId(skuId).getData();
        if (!CollectionUtils.isEmpty(skuAttrValueEntities)) {
            Map<Long, String> saleAttr = skuAttrValueEntities.stream().collect(Collectors.toMap(SkuAttrValueEntity::getAttrId, SkuAttrValueEntity::getAttrValue));
            itemVo.setSaleAttr(saleAttr);
        }
        itemVo.setSkusJson(pmsClient.querySaleAttrMappingBySpuId(spuId).getData());

        // ---------- 商品描述（图片列表） ----------
        SpuDescEntity spuDescEntity = pmsClient.querySpuDescById(spuId).getData();
        if (spuDescEntity != null) {
            itemVo.setSpuImages(Arrays.asList(StringUtils.split(spuDescEntity.getDecript(), ",")));
        }

        // ---------- 规格与包装 ----------
        itemVo.setGroups(pmsClient.queryGroupAttrValue(cid, spuId, skuId).getData());
        return itemVo;
        =========================================== 使用同步方式远程调用实现功能<结束> ===========================================
         */
    }
}
