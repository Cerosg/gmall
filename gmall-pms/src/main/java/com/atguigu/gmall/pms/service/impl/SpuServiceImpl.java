package com.atguigu.gmall.pms.service.impl;

import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.pms.feign.GmallSmsClient;
import com.atguigu.gmall.pms.mapper.SkuMapper;
import com.atguigu.gmall.pms.mapper.SpuDescMapper;
import com.atguigu.gmall.pms.mapper.SpuMapper;
import com.atguigu.gmall.pms.service.SkuAttrValueService;
import com.atguigu.gmall.pms.service.SkuImagesService;
import com.atguigu.gmall.pms.service.SpuAttrValueService;
import com.atguigu.gmall.pms.service.SpuService;
import com.atguigu.gmall.pms.vo.SpuBaseAttrVo;
import com.atguigu.gmall.pms.vo.SpuSkuVo;
import com.atguigu.gmall.pms.vo.SpuVo;
import com.atguigu.gmall.sms.vo.SkuSaleVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service("spuService")
public class SpuServiceImpl extends ServiceImpl<SpuMapper, SpuEntity> implements SpuService {
    @Autowired
    private SpuDescMapper spuDescMapper;
    @Autowired
    private SpuAttrValueService spuAttrValueService;
    @Autowired
    private SkuMapper skuMapper;
    @Autowired
    private SkuImagesService skuImagesService;
    @Autowired
    private SkuAttrValueService skuAttrValueService;
    @Autowired
    private GmallSmsClient smsClient;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<SpuEntity> page = this.page(paramVo.getPage(), new QueryWrapper<>());
        return new PageResultVo(page);
    }

    @Override
    public PageResultVo querySpuList(PageParamVo paramVo, Long cid) {
        QueryWrapper<SpuEntity> wrapper = new QueryWrapper<>();
        // 0-查询全部分类；其他-查询指定分类stat
        if (cid != 0)
            wrapper.eq("category_id", cid);
        // 通用分页参数key就是spuid,spuname的检索条件
        String key = paramVo.getKey();
        if (StringUtils.isNotBlank(key)) {
            // 商品名称中包含关键字或者商品id等于关键字
            wrapper.and(t -> t.like("name", key).or().eq("id", key));
        }
        return new PageResultVo(this.page(paramVo.getPage(), wrapper));
    }

    //@Transactional(rollbackFor = Exception.class)
    @GlobalTransactional
    @Override
    public void bigSave(SpuVo spuVo) {
        // 一、------------------------------ 保存spu相关信息 ------------------------------
        // 1.保存spu基本信息 pms_spu
        Long spuId = saveSpu(spuVo);
        // 2.保存spu图片信息 pms_spu_desc
        List<String> images = saveSpuDesc(spuVo, spuId);
        // 3.保存规格参数等信息 pms_spu_attr_value
        saveSpuAttrValue(spuVo, spuId);
        // 二、------------------------------ 保存sku相关信息 ------------------------------
        saveSku(spuVo, spuId, images);

        // 完成大保存之后，利用RabbitMQ发送消息，实现数据同步
        // convertAndSend(交换机名称，RoutingKey，要发送的消息)
        rabbitTemplate.convertAndSend("PMS-ITEM-EXCHANGE", "item.insert", spuId);
    }

    /**
     * 保存sku相关信息
     *
     * @param spuVo
     * @param spuId
     * @param images
     */
    private void saveSku(SpuVo spuVo, Long spuId, List<String> images) {
        List<SpuSkuVo> skus = spuVo.getSkus();
        if (CollectionUtils.isEmpty(skus)) {
            return;
        }
        skus.forEach(sku -> {
            // 1.保存sku基本信息
            sku.setSpuId(spuId);
            sku.setBrandId(spuVo.getBrandId());
            sku.setCatagoryId(spuVo.getCategoryId());
            List<String> images1 = sku.getImages();
            if (!CollectionUtils.isEmpty(images1))
                sku.setDefaultImage(StringUtils.isNotBlank(sku.getDefaultImage()) ? sku.getDefaultImage() : images.get(0));
            skuMapper.insert(sku);
            Long skuId = sku.getId();
            // 2.保存sku描述信息
            if (!CollectionUtils.isEmpty(images)) {
                List<SkuImagesEntity> skuImagesEntities = images.stream().map(image -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setUrl(image);
                    skuImagesEntity.setDefaultStatus(StringUtils.equals(image, sku.getDefaultImage()) ? 1 : 0);
                    return skuImagesEntity;
                }).collect(Collectors.toList());
                skuImagesService.saveBatch(skuImagesEntities);
            }
            // 3.保存sku规格参数信息（销售属性）
            List<SkuAttrValueEntity> saleAttrs = sku.getSaleAttrs();
            if (!CollectionUtils.isEmpty(saleAttrs)) {
                saleAttrs.forEach(skuAttr -> skuAttr.setSkuId(skuId));
                skuAttrValueService.saveBatch(saleAttrs);
            }
            // 三、------------------------------ 保存sms相关信息 ------------------------------
            SkuSaleVo skuSaleVo = new SkuSaleVo();
            BeanUtils.copyProperties(sku, skuSaleVo);
            skuSaleVo.setSkuId(skuId);
            smsClient.saveSkuSaleAttrs(skuSaleVo);
        });
    }

    /**
     * 保存spu属性值
     *
     * @param spuVo
     * @param spuId
     */
    private void saveSpuAttrValue(SpuVo spuVo, Long spuId) {
        List<SpuBaseAttrVo> baseAttrs = spuVo.getBaseAttrs();
        if (!CollectionUtils.isEmpty(baseAttrs)) {
            List<SpuAttrValueEntity> spuAttrValueEntities = baseAttrs.stream().map(spuAttr -> {
                SpuAttrValueEntity spuAttrValueEntity = new SpuAttrValueEntity();
                BeanUtils.copyProperties(spuAttr, spuAttrValueEntity);
                spuAttrValueEntity.setSpuId(spuId);
                return spuAttrValueEntity;
            }).collect(Collectors.toList());
            spuAttrValueService.saveBatch(spuAttrValueEntities);
        }
    }

    /**
     * 保存spu描述信息
     *
     * @param spuVo
     * @param spuId
     * @return
     */
    private List<String> saveSpuDesc(SpuVo spuVo, Long spuId) {
        List<String> images = spuVo.getSpuImages();
        if (!CollectionUtils.isEmpty(images))
            spuDescMapper.insert(new SpuDescEntity(spuId, StringUtils.join(images, ",")));
        return images;
    }

    /**
     * 保存spu基本信息
     *
     * @param spuVo
     * @return
     */
    private Long saveSpu(SpuVo spuVo) {
        spuVo.setCreateTime(new Date());
        spuVo.setUpdateTime(spuVo.getCreateTime());
        spuVo.setPublishStatus(1);
        save(spuVo);
        return spuVo.getId();
    }
}