package com.atguigu.gmall.oms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.PageParamVo;
import com.atguigu.gmall.oms.entity.OrderItemEntity;

import java.util.Map;

/**
 * 订单项信息
 *
 * @author Cerosg
 * @email Cerosg@outlook.com
 * @date 2020-07-20 20:40:03
 */
public interface OrderItemService extends IService<OrderItemEntity> {

    PageResultVo queryPage(PageParamVo paramVo);
}

