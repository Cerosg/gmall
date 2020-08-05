package com.atguigu.gmall.search.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月01日 13时49分
 */
@Data
public class SearchParamVo {
    // 筛选地址栏参数：?keyword=手机&cid=225,230&brandId=2,3&props=8:8G-12G&props=9:128G-256G&sort=1&priceFrom=1000&priceTo=20000

    private String keyword;// 关键字
    private List<Long> cid;// 分类id
    private List<Long> brandId;// 品牌id
    private List<String> props;// 规格参数
    /*
     * 1：价格升序
     * 2：价格降序
     * 3：销量降序
     * 4：新品降序
     */
    private Integer sort;// 排序字段
    // 价格区间
    private Double priceFrom;// 价格起点（可无）
    private Double priceTo;// 价格终点（可无）
    // 页码
    private Integer pageNum = 1;
    private final Integer pageSize = 20;
    // 库存（是否有货）
    private Boolean stock;
}
