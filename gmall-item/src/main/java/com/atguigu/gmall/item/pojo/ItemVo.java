package com.atguigu.gmall.item.pojo;

import com.atguigu.gmall.pms.entity.CategoryEntity;
import com.atguigu.gmall.pms.entity.SkuImagesEntity;
import com.atguigu.gmall.pms.vo.ItemGroupVo;
import com.atguigu.gmall.pms.vo.SaleAttrValueVo;
import com.atguigu.gmall.sms.vo.ItemSaleVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月07日 09时14分
 */
@Data
public class ItemVo {
    // ---------- 面包屑部分 ----------
    // 一二三级分类
    private List<CategoryEntity> categories;
    // 品牌
    private Long brandId;
    private String brandName;
    // spu名称
    private Long spuId;
    private String spuName;

    // ---------- sku相关属性 ----------
    private Long skuId;
    private String defaultImage;// 默认大图片
    private String title;
    private String subTitle;
    private BigDecimal price;
    private Integer weight;
    private List<SkuImagesEntity> images; // sku的图片列表

    // ---------- sale相关属性 ----------
    /**
     * 举例：
     * 【赠品】× 1× 1（赠完即止）等促销消息
     * 【限购】 该商品购买1-10件时享受单件价￥1189.00，超出数量以结算价为准
     * 【换购】 购买1件可优惠换购热销商品 立即换购 >>
     * 【满额返券】 购通讯部分商品满1元返家电815周年庆190元大礼包，限部分商品使用
     * …………
     * 数据提取：【类型】+描述
     */
    private List<ItemSaleVo> sales;
    private Boolean stock = false;// 是否有货
    /**
     * 选择颜色：[钛银黑钛银黑] * [冰海蓝冰海蓝] * [蜜桃金蜜桃金] * [国风雅灰]
     * 选择版本: [8GB+128GB] * [8GB+256GB] * [12GB+256GB]
     * …………
     * 数据提取：
     * 属性名称：属性值集合
     */
    private List<SaleAttrValueVo> saleAttrs;// 用户选择销售属性集合
    private Map<Long, String> saleAttr;// 当前选中的sku的销售属性：{4:8G，5:128G，6:钛金黑}
    // {‘8G，128G，暗夜黑’:100，‘12G，256G，冰海蓝’:101，‘8G，128G，蜜桃粉’:102}
    private String skusJson;// 当前sku所属spu下，所有sku的组合

    // ---------- 商品描述（图片列表） ----------
    private List<String> spuImages;

    // ---------- 规格与包装 ----------
    private List<ItemGroupVo> groups;// 规格参数组及组下的规格参数与值
}
