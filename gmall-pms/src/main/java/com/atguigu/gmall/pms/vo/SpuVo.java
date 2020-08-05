package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.SpuEntity;
import lombok.Data;

import java.util.List;

/**
 * @author Cerosg
 * @describable
 * @create 2020年07月23日 10时41分
 */
@Data
public class SpuVo extends SpuEntity {
    // spu描述
    private List<String> spuImages;
    // attr
    private List<SpuBaseAttrVo> baseAttrs;
    // sku
    private List<SpuSkuVo> skus;
}
