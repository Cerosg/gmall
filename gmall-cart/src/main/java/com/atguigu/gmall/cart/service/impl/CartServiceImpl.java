package com.atguigu.gmall.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.cart.entity.Cart;
import com.atguigu.gmall.cart.feign.GmallPmsClient;
import com.atguigu.gmall.cart.feign.GmallSmsClient;
import com.atguigu.gmall.cart.feign.GmallWmsClient;
import com.atguigu.gmall.cart.interceptor.LoginInterceptor;
import com.atguigu.gmall.cart.service.CartAsyncService;
import com.atguigu.gmall.cart.service.CartService;
import com.atguigu.gmall.common.bean.UserInfo;
import com.atguigu.gmall.pms.entity.SkuAttrValueEntity;
import com.atguigu.gmall.pms.entity.SkuEntity;
import com.atguigu.gmall.wms.entity.WareSkuEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Cerosg
 * @create 2020年08月11日 17时37分
 */
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private StringRedisTemplate redisTemplate;
    private static final String KEY_PREFIX = "cart:info:";

    @Autowired
    private GmallPmsClient gmallPmsClient;
    @Autowired
    private GmallSmsClient gmallSmsClient;
    @Autowired
    private GmallWmsClient gmallWmsClient;
    @Autowired
    private CartAsyncService cartAsyncService;

    @Override
    public void addCart(Cart cart) {
        // 1.获取用户登录信息
        String userId;
        UserInfo userInfo = LoginInterceptor.getUserInfo();// 从拦截器中获取用户信息
        userId = userInfo.getUserId() != null ? userInfo.getUserId().toString() : userInfo.getUserKey();
        String key = KEY_PREFIX + userId;
        // 获取内层map的操作对象
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(key);
        // 2.判断该用户的购物车中是否有该商品：有-更新数量；无-添加
        Long skuIdLong = cart.getSkuId();// 用户提交的skuId
        String skuId = skuIdLong.toString();
        BigDecimal count = cart.getCount();// 用户提交skuId商品对应的数量
        if (hashOps.hasKey(skuId)) {
            // 有-更新数量
            String cartJson = hashOps.get(skuId).toString();// 获取指定skuId商品对应的json对象字符串
            cart = JSON.parseObject(cartJson, Cart.class);// 反序列化json字符串
            cart.setCount(count.add(cart.getCount()));// 在原有商品数量上更新数量
            // TODO: 2020/8/11  异步更新购物车数量
            this.cartAsyncService.updateCartByUserIdAndSkuId(userId, cart);
        } else {
            // 无-添加
            cart.setUserId(userId);
            cart.setCheck(true);// 设置选商品中状态
            SkuEntity skuEntity = gmallPmsClient.querySkuById(skuIdLong).getData();
            if (skuEntity != null) {
                cart.setTitle(skuEntity.getTitle());
                cart.setDefaultImage(skuEntity.getDefaultImage());
                cart.setPrice(skuEntity.getPrice());
            }
            List<WareSkuEntity> wareSkuEntities = gmallWmsClient.queryWareSkuBySkuId(skuIdLong).getData();
            if (!CollectionUtils.isEmpty(wareSkuEntities)) {
                cart.setStore(wareSkuEntities.stream().anyMatch(wareSkuEntity -> wareSkuEntity.getStock() - wareSkuEntity.getStockLocked() > 0));
            }
            List<SkuAttrValueEntity> skuAttrValueEntities = gmallPmsClient.querySaleAttrValueVoBySkuId(skuIdLong).getData();
            cart.setSaleAttrs(JSON.toJSONString(skuAttrValueEntities));
            cart.setSales(JSON.toJSONString(gmallSmsClient.querySalesBySkuId(skuIdLong).getData()));
            // TODO: 2020/8/11 异步添加至redis和MySQL数据库中
            this.cartAsyncService.addCart(userId, cart);
        }
        // 3.将购物车添加到redis中Map<String , String>
        hashOps.put(skuId, JSON.toJSONString(cart));
    }

    @Override
    public Cart queryCartBySkuId(Long skuId) {
        String key = KEY_PREFIX;
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        key += userInfo.getUserId() != null ? userInfo.getUserId().toString() : userInfo.getUserKey();
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(key);
        if (hashOps.hasKey(skuId.toString())) {
            String cartJson = hashOps.get(skuId.toString()).toString();
            return JSON.parseObject(cartJson, Cart.class);
        } else {
            throw new RuntimeException("该用户的购物车中没有该商品！");
        }
    }

    @Override
    public List<Cart> queryCarts() {
        // 1.查询未登录的购物车
        UserInfo userInfo = LoginInterceptor.getUserInfo();
        String key = KEY_PREFIX + userInfo.getUserKey();
        BoundHashOperations<String, Object, Object> hashOps = redisTemplate.boundHashOps(key);
        // 从操作对象中获取未登录购物车集合
        List<Object> unLoginCartJsons = hashOps.values();
        List<Cart> unLoginCarts = null;
        if (!CollectionUtils.isEmpty(unLoginCartJsons)) {
            unLoginCarts = unLoginCartJsons.stream()
                    .map(unLoginCart -> JSON.parseObject(unLoginCart.toString(), Cart.class))
                    .collect(Collectors.toList());
        }
        // 2.判断登录状态
        Long userId = userInfo.getUserId();
        if (userId == null) {
            // 3.未登录：返回未登录的购物车列表
            return unLoginCarts;
        }
        // 4.已登录：合并购物车列表并返回
        key = KEY_PREFIX + userId;
        if (!CollectionUtils.isEmpty(unLoginCarts)) {
            BoundHashOperations<String, Object, Object> loginHashOps = redisTemplate.boundHashOps(key);
            unLoginCarts.forEach(cart -> {
                if (loginHashOps.hasKey(cart.getSkuId().toString())) {

                }
            });
        }
        // 5.删除未登录的购物车

        // 6.查询登录状态的购物车
        return null;
    }
}
