package com.atguigu.gmall.pms.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 商品三级分类
 *
 * @author Cerosg
 * @email Cerosg@outlook.com
 * @date 2020-07-20 19:34:59
 */
@Data
@TableName("pms_category")
public class CategoryEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 分类id
     */
    @TableId
    private Long id;
    /**
     * 分类名称
     */
    private String name;
    /**
     * 父分类id
     */
    private Long parentId;
    /**
     * 是否显示[0-不显示，1显示]
     */
    private Integer status;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 图标地址
     */
    private String icon;
    /**
     * 计量单位
     */
    private String unit;
    /**
     * 子分类列表，非必须字段，用@TableField(exist = false)标记
     */
    @TableField(exist = false)
    private List<CategoryEntity> subs;

}
