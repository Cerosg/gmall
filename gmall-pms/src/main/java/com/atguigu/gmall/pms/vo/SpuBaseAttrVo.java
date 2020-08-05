package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.SpuAttrValueEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author Cerosg
 * @describable
 * @create 2020年07月23日 10时48分
 */
public class SpuBaseAttrVo extends SpuAttrValueEntity {
    public void setValueSelected(List<String> valueSelected) {
        if (!CollectionUtils.isEmpty(valueSelected))
            this.setAttrValue(StringUtils.join(valueSelected, ","));
    }
}
