package com.atguigu.gmall.cart.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName("cart_info")
public class Cart {

    @TableId
    private Long id;
    @TableField("user_id")
    private String userId;
    @TableField("`check`") // check是mysql的关键字，所以这里要加'`'号
    private Boolean check; // 选中状态

    @TableField("sku_id")
    private Long skuId;
    private String defaultImage;
    private String title;
    private BigDecimal price; // 加入购物车时的价格
    @TableField(exist = false)// 数据库中不存在，页面显示需要使用的字段
    private BigDecimal currentPrice; // 实时价格

    private String sales; // 营销信息: List<ItemSaleVo>的json格式
    @TableField("sale_attrs")
    private String saleAttrs; // 销售属性：List<SkuAttrValueEntity>的json格式

    private BigDecimal count;
    private Boolean store = false; // 是否有货
}