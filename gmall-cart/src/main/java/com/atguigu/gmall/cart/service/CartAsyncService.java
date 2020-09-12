package com.atguigu.gmall.cart.service;

import com.atguigu.gmall.cart.entity.Cart;

public interface CartAsyncService {
    void updateCartByUserIdAndSkuId(String userId, Cart cart);

    void addCart(String userId, Cart cart);
}