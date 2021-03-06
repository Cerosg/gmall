package com.atguigu.gmall.pms.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.pms.entity.SkuAttrValueEntity;
import com.atguigu.gmall.pms.entity.SkuEntity;
import com.atguigu.gmall.pms.mapper.SkuAttrValueMapper;
import com.atguigu.gmall.pms.mapper.SkuMapper;
import com.atguigu.gmall.pms.service.SkuAttrValueService;
import com.atguigu.gmall.pms.vo.SaleAttrValueVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service("skuAttrValueService")
public class SkuAttrValueServiceImpl extends ServiceImpl<SkuAttrValueMapper, SkuAttrValueEntity> implements SkuAttrValueService {

    @Autowired
    private SkuAttrValueMapper skuAttrValueMapper;
    @Autowired
    private SkuMapper skuMapper;

    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        return new PageResultVo(this.page(paramVo.getPage(), new QueryWrapper<>()));
    }

    @Override
    public List<SkuAttrValueEntity> querySearchAttrValueBySkuId(Long skuId) {
        return skuAttrValueMapper.querySearchAttrValueBySkuId(skuId);
    }

    @Override
    public List<SaleAttrValueVo> querySaleAttrValueVoBySpuId(Long spuId) {
        List<SkuEntity> skuEntities = skuMapper.selectList(new QueryWrapper<SkuEntity>().eq("spu_id", spuId));
        if (CollectionUtils.isEmpty(skuEntities))
            return null;
        List<Long> skuIds = skuEntities.stream().map(SkuEntity::getId).collect(Collectors.toList());
        List<SkuAttrValueEntity> skuAttrValueEntities = this.list(new QueryWrapper<SkuAttrValueEntity>().in("sku_id", skuIds));
        if (CollectionUtils.isEmpty(skuAttrValueEntities))
            return null;
        Map<Long, List<SkuAttrValueEntity>> map = skuAttrValueEntities.stream().collect(Collectors.groupingBy(SkuAttrValueEntity::getAttrId));
        List<SaleAttrValueVo> saleAttrValueVos = new ArrayList<>();
        map.forEach((attrId, attrValueEntities) -> {
            SaleAttrValueVo saleAttrValueVo = new SaleAttrValueVo();
            saleAttrValueVo.setAttrId(attrId);
            if (!CollectionUtils.isEmpty(attrValueEntities)) {
                saleAttrValueVo.setAttrName(attrValueEntities.get(0).getAttrName());
                Set<String> collect = attrValueEntities.stream().map(SkuAttrValueEntity::getAttrValue).collect(Collectors.toSet());
                saleAttrValueVo.setAttrValues(collect);
            }
            saleAttrValueVos.add(saleAttrValueVo);
        });
        return saleAttrValueVos;
    }

    @Override
    public String querySaleAttrMappingBySpuId(Long spuId) {
        List<Map<String, Object>> maps = this.skuAttrValueMapper.querySaleAttrMappingBySpuId(spuId);
//        System.out.println(maps);
        Map<String, Long> skusMap = maps.stream().collect(Collectors.toMap(map -> map.get("attr_values").toString(), map -> (Long) map.get("sku_id")));
        return JSON.toJSONString(skusMap);
    }
}



















