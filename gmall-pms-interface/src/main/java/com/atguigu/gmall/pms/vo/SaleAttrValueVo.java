package com.atguigu.gmall.pms.vo;

import lombok.Data;

import java.util.Set;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月07日 09时41分
 */
@Data
public class SaleAttrValueVo {
    // 选择颜色：[钛银黑钛银黑] * [冰海蓝冰海蓝] * [蜜桃金蜜桃金] * [国风雅灰]
    private Long attrId;
    private String attrName;
    private Set<String> attrValues;
}
