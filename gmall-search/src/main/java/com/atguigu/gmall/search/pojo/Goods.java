package com.atguigu.gmall.search.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 * @describable 商城首页商品展示对应的实体类
 * @create 2020年07月30日 16时37分
 */
@Data
@Document(indexName = "goods", type = "info", shards = 3, replicas = 2)
public class Goods {
    // 商品详情
    @Id
    private Long skuId;// 商品skuId
    @Field(type = FieldType.Keyword, index = false)
    private String image;// 商品图片url
    @Field(type = FieldType.Double)
    private Double price;// 商品价格
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String title;// 商品标题
    @Field(type = FieldType.Keyword, index = false)
    private String subTitle;// 商品副标题

    // 排序和筛选
    @Field(type = FieldType.Long)
    private Long sales;// 销量
    @Field(type = FieldType.Date)
    private Date createTime;// 新品
    @Field(type = FieldType.Boolean)
    private Boolean stock;// 库存

    // === 聚合字段 ===
    // 1. 品牌
    @Field(type = FieldType.Long)
    private Long brandId;
    @Field(type = FieldType.Keyword)
    private String brandName;
    @Field(type = FieldType.Keyword)
    private String logo;
    // 2. 分类
    @Field(type = FieldType.Long)
    private Long categoryId;
    @Field(type = FieldType.Keyword)
    private String categoryName;

    // 规格参数
    @Field(type = FieldType.Nested)
    private List<SearchAttrValue> searchAttrs;
}
