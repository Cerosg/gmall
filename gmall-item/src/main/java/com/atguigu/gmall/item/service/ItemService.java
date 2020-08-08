package com.atguigu.gmall.item.service;

import com.atguigu.gmall.item.pojo.ItemVo;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月07日 20时42分
 */
public interface ItemService {
    ItemVo loadData(Long skuId);
}
