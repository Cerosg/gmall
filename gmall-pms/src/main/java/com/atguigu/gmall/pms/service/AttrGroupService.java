package com.atguigu.gmall.pms.service;

import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.pms.entity.AttrGroupEntity;
import com.atguigu.gmall.pms.vo.GroupVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 属性分组
 *
 * @author Cerosg
 * @email Cerosg@outlook.com
 * @date 2020-07-20 19:34:59
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageResultVo queryPage(PageParamVo paramVo);

    List<GroupVo> queryAttrGroupAndValueByCid(Long id);
}

