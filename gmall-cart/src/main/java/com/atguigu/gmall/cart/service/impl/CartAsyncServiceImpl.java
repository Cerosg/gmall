package com.atguigu.gmall.cart.service.impl;

import com.atguigu.gmall.cart.entity.Cart;
import com.atguigu.gmall.cart.mapper.CartMapper;
import com.atguigu.gmall.cart.service.CartAsyncService;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class CartAsyncServiceImpl implements CartAsyncService {
    @Autowired
    private CartMapper cartMapper;

    /**
     * 当购物车中已有选购商品时，异步更新数量
     *
     * @param userId
     * @param cart
     */
    @Override
    @Async
    public void updateCartByUserIdAndSkuId(String userId, Cart cart) {
        this.cartMapper.update(cart, new UpdateWrapper<Cart>().eq("user_id", userId).eq("sku_id", cart.getSkuId()));
    }

    /**
     * 异步向MySQL数据库中添加购物车记录
     *
     * @param userId
     * @param cart
     */
    @Override
    @Async
    public void addCart(String userId, Cart cart) {
        this.cartMapper.insert(cart);
    }
}