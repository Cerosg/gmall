package com.atguigu.gmall.pms.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 属性分组
 *
 * @author Cerosg
 * @email Cerosg@outlook.com
 * @date 2020-07-20 19:34:59
 */
@Data
@TableName("pms_attr_group")
public class AttrGroupEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 分组id
     */
    @TableId
    private Long id;
    /**
     * 组名
     */
    private String name;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 组图标
     */
    private String icon;
    /**
     * 所属分类id
     */
    private Long categoryId;
    /**
     * 备注
     */
    private String remark;
}
