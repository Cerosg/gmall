package com.atguigu.gmall.sms.service.impl;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.sms.entity.SkuBoundsEntity;
import com.atguigu.gmall.sms.entity.SkuFullReductionEntity;
import com.atguigu.gmall.sms.entity.SkuLadderEntity;
import com.atguigu.gmall.sms.mapper.SkuBoundsMapper;
import com.atguigu.gmall.sms.mapper.SkuFullReductionMapper;
import com.atguigu.gmall.sms.mapper.SkuLadderMapper;
import com.atguigu.gmall.sms.service.SkuBoundsService;
import com.atguigu.gmall.sms.vo.ItemSaleVo;
import com.atguigu.gmall.sms.vo.SkuSaleVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service("skuBoundsService")
public class SkuBoundsServiceImpl extends ServiceImpl<SkuBoundsMapper, SkuBoundsEntity> implements SkuBoundsService {

    @Autowired
    private SkuFullReductionMapper reductionMapper;

    @Autowired
    private SkuLadderMapper ladderMapper;

    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<SkuBoundsEntity> page = this.page(paramVo.getPage(), new QueryWrapper<>());
        return new PageResultVo(page);
    }

    @Transactional
    @Override
    public void saveSkuSaleAttrs(SkuSaleVo skuSaleVo) {
        // 1.保存积分信息
        SkuBoundsEntity skuBoundsEntity = new SkuBoundsEntity();
        BeanUtils.copyProperties(skuSaleVo, skuBoundsEntity);
        List<Integer> work = skuSaleVo.getWork();
        if (!CollectionUtils.isEmpty(work))
            skuBoundsEntity.setWork(work.get(3) * 8 + work.get(2) * 4 + work.get(1) * 2 + work.get(0));
        save(skuBoundsEntity);
        // 2.保存满减信息
        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuSaleVo, skuFullReductionEntity);
        skuFullReductionEntity.setAddOther(skuSaleVo.getFullAddOther());
        reductionMapper.insert(skuFullReductionEntity);
        // 3.保存折扣信息
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        BeanUtils.copyProperties(skuSaleVo, skuLadderEntity);
        skuLadderEntity.setAddOther(skuSaleVo.getLadderAddOther());
        ladderMapper.insert(skuLadderEntity);
    }

    @Override
    public List<ItemSaleVo> querySalesBySkuId(Long skuId) {
        List<ItemSaleVo> itemSaleVos = new ArrayList<>();
        // 积分优惠
        SkuBoundsEntity boundsEntity = getOne(new QueryWrapper<SkuBoundsEntity>().eq("sku_id", skuId));
        if (boundsEntity != null) {
            ItemSaleVo bounds = new ItemSaleVo();
            bounds.setType("积分");
            bounds.setDesc("送" + boundsEntity.getGrowBounds().setScale(2, BigDecimal.ROUND_HALF_UP) + "成长积分，送" + boundsEntity.getBuyBounds().setScale(2, BigDecimal.ROUND_HALF_UP) + "购物积分");
            itemSaleVos.add(bounds);
        }
        // 满减优惠
        SkuFullReductionEntity reductionEntity = reductionMapper.selectOne(new QueryWrapper<SkuFullReductionEntity>().eq("sku_id", skuId));
        if (reductionEntity != null) {
            ItemSaleVo reduction = new ItemSaleVo();
            reduction.setType("满减");
            reduction.setDesc("满" + reductionEntity.getFullPrice().setScale(2, BigDecimal.ROUND_HALF_UP) + "，减" + reductionEntity.getReducePrice().setScale(2, BigDecimal.ROUND_HALF_UP));
            itemSaleVos.add(reduction);
        }
        // 折扣优惠
        SkuLadderEntity ladderEntity = ladderMapper.selectOne(new QueryWrapper<SkuLadderEntity>().eq("sku_id", skuId));
        if (ladderEntity != null) {
            ItemSaleVo ladder = new ItemSaleVo();
            ladder.setType("折扣");
            ladder.setDesc("满" + ladderEntity.getFullCount() + "件，打" + ladderEntity.getDiscount().divide(new BigDecimal(10)).setScale(1, BigDecimal.ROUND_HALF_UP) + "折");
            itemSaleVos.add(ladder);
        }
        return itemSaleVos;
    }
}