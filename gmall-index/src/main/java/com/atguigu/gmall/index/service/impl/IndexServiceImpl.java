package com.atguigu.gmall.index.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.client.naming.utils.CollectionUtils;
import com.alibaba.nacos.client.utils.StringUtils;
import com.atguigu.gmall.index.feign.GmallPmsClient;
import com.atguigu.gmall.index.service.IndexService;
import com.atguigu.gmall.pms.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月05日 09时51分
 */
@Service
public class IndexServiceImpl implements IndexService {
    @Autowired
    private GmallPmsClient gmallPmsClient;
    @Autowired
    private StringRedisTemplate redisTemplate;
    // 通常情况下，redis缓存中key的前缀：“模块名:数据模型:”
    private final static String KEY_PREFIX = "index:category:";

    // 一级分类
    @Override
    public List<CategoryEntity> getCategoryLv1ById() {
        // 先查询缓存，如果命中，直接返回
        String cacheJson = redisTemplate.opsForValue().get(KEY_PREFIX + "0");
        if (StringUtils.isNotBlank(cacheJson)) {
            return JSON.parseArray(cacheJson, CategoryEntity.class);
        }
        List<CategoryEntity> categoryEntities = gmallPmsClient.queryCategoryByPid(0L).getData();
        if (!CollectionUtils.isEmpty(categoryEntities))
            // 将查询到的结果集存入redis中，缓存的键可以设计为”一级分类的id“
            redisTemplate.opsForValue().set(KEY_PREFIX + "0", JSON.toJSONString(categoryEntities), 14, TimeUnit.DAYS);
        return categoryEntities;
    }

    // 二、三级分类(需要在配置文件中配置redis密码)
    @Override
    public List<CategoryEntity> querySubMenuByPid(Long pid) {
        // 先查询缓存，如果命中，直接返回
        String cacheJson = redisTemplate.opsForValue().get(KEY_PREFIX + pid);
        if (StringUtils.isNotBlank(cacheJson)) {
            return JSON.parseArray(cacheJson, CategoryEntity.class);
        }
        // 查询某个id的分类的子分类
        List<CategoryEntity> categoryEntities = gmallPmsClient.querySubMenuByPid(pid).getData();
        if (!CollectionUtils.isEmpty(categoryEntities))
            // 将查询到的结果集存入redis中，缓存的键可以设计为”一级分类的id“
            redisTemplate.opsForValue().set(KEY_PREFIX + pid, JSON.toJSONString(categoryEntities), 14, TimeUnit.DAYS);
        return categoryEntities;
    }
}
