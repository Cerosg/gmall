package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.SkuAttrValueEntity;
import com.atguigu.gmall.pms.entity.SkuEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Cerosg
 * @describable
 * @create 2020年07月23日 10时50分
 */
@Data
public class SpuSkuVo extends SkuEntity {
    // sku图片
    List<String> images;

    // 满减信息接收
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private Integer fullAddOther;

    // 积分相关信息接收
    private BigDecimal growBounds;
    private BigDecimal buyBounds;
    private List<Integer> work;

    // 打折相关信息接收
    private Integer fullCount;
    private BigDecimal discount;
    private Integer ladderAddOther;

    // 销售属性
    List<SkuAttrValueEntity> saleAttrs;
}
