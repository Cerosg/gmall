package com.atguigu.gmall.index.service.impl;

import com.atguigu.gmall.index.aspect.GmallRedisCache;
import com.atguigu.gmall.index.feign.GmallPmsClient;
import com.atguigu.gmall.index.service.IndexService;
import com.atguigu.gmall.pms.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月05日 09时51分
 */
@Service
public class IndexServiceImpl implements IndexService {
    @Autowired
    private GmallPmsClient gmallPmsClient;
    // 通常情况下，redis缓存中key的前缀：“模块名:数据模型:”
    private final static String KEY_PREFIX = "index:category:";

    // 一级分类
    @GmallRedisCache(prefix = KEY_PREFIX, lock = "lock", timeout = 20160, random = 1440)
    @Override
    public List<CategoryEntity> getCategoryLv1ById() {
        return gmallPmsClient.queryCategoryByPid(0L).getData();
    }

    // 二、三级分类(需要在配置文件中配置redis密码)
    @Override
    @GmallRedisCache(prefix = KEY_PREFIX, lock = "lock", timeout = 20160, random = 1440)
    public List<CategoryEntity> querySubMenuByPid(Long pid) {
        return gmallPmsClient.querySubMenuByPid(pid).getData();
    }
}
