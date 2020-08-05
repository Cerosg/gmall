package com.atguigu.gmall.search.pojo;

import com.atguigu.gmall.pms.entity.BrandEntity;
import com.atguigu.gmall.pms.entity.CategoryEntity;
import lombok.Data;

import java.util.List;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月03日 08时31分
 */
@Data
public class SearchResponseVo {
    // 封装品牌过滤条件
    private List<BrandEntity> brands;
    // 封装分类过滤条件
    private List<CategoryEntity> categories;
    // 封装规格参数过滤条件
    private List<SearchResponseAttrVo> filters;
    // 封装分页数据
    private Integer pageNum;// 页码
    private Integer pageSize;// 页面记录数
    private Long total;// 总记录数，总记录数÷页面记录数=总页码
    // 页面商品数据
    private List<Goods> goodsList;
}
