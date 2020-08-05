package com.atguigu.gmall.search.repository;

import com.atguigu.gmall.search.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author Cerosg
 * @describable
 * @create 2020年07月31日 15时55分
 */
public interface GoodsRepository extends ElasticsearchRepository<Goods, Long> {
}
