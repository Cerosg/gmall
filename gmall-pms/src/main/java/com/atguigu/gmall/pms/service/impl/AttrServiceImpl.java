package com.atguigu.gmall.pms.service.impl;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.pms.entity.AttrEntity;
import com.atguigu.gmall.pms.mapper.AttrMapper;
import com.atguigu.gmall.pms.service.AttrService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrMapper, AttrEntity> implements AttrService {

    @Override
    public PageResultVo queryPage(PageParamVo paramVo) {
        IPage<AttrEntity> page = this.page(paramVo.getPage(), new QueryWrapper<>());
        return new PageResultVo(page);
    }

    @Override
    public List<AttrEntity> queryAttrsByGid(Long cid, Integer type, Integer searchType) {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        if (cid != 0)// 如果传入的id为0时，查询所有属性信息
            wrapper.eq("category_id", cid);
        if (type != null)
            wrapper.eq("type", type);
        if (searchType != null)
            wrapper.eq("search_type", searchType);
        return list(wrapper);
    }
}