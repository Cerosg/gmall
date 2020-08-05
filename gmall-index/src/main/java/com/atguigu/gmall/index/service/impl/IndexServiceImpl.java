package com.atguigu.gmall.index.service.impl;

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

    @Override
    public List<CategoryEntity> getCategoryLv1ById() {
        return gmallPmsClient.queryCategoryByPid(0L).getData();
    }

    @Override
    public List<CategoryEntity> querySubMenuByPid(Long pid) {
        return gmallPmsClient.querySubMenuByPid(pid).getData();
    }
}
