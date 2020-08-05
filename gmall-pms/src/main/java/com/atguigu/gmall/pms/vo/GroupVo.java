package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.AttrEntity;
import com.atguigu.gmall.pms.entity.AttrGroupEntity;
import lombok.Data;

import java.util.List;

/**
 * 返回 “查询分类下的组及规格参数” 的值对象
 *
 * @author Cerosg
 * @describable
 * @create 2020年07月23日 09时09分
 */
@Data
public class GroupVo extends AttrGroupEntity {
    /**
     * 属性值
     */
    private List<AttrEntity> attrEntities;
}
