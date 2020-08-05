package com.atguigu.gmall.sms.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Cerosg
 * @describable
 * @create 2020年07月23日 16时48分
 */
@Data
public class SkuSaleVo {
    private Long skuId;
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
}
