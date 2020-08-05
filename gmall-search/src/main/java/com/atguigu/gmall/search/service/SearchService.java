package com.atguigu.gmall.search.service;

import com.atguigu.gmall.search.pojo.SearchParamVo;
import com.atguigu.gmall.search.pojo.SearchResponseVo;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月01日 14时52分
 */
public interface SearchService {
    /**
     * 根据商品查询页的查询字段，在ElasticSearch索引库中查询数据
     *
     * @param searchParamVo 查询字段封装成的对象
     * @return 根据查询字段查到的结果集
     */
    SearchResponseVo search(SearchParamVo searchParamVo);

    /**
     * 根据pms服务中大保存方法完成后生成的消息，在ElasticSearch中创建索引
     *
     * @param spuId 大保存向MySQL数据库中插入的spu的id
     */
    void createIndex(Long spuId);
}
