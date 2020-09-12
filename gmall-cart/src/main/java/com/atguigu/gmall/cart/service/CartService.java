package com.atguigu.gmall.cart.service;

import com.atguigu.gmall.cart.entity.Cart;

import java.util.List;

/**
 * @author Cerosg
 * @create 2020年08月11日 17时36分
 */
public interface CartService {
    void addCart(Cart cart);

    Cart queryCartBySkuId(Long skuId);

    List<Cart> queryCarts();
}
