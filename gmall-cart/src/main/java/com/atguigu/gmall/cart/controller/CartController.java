package com.atguigu.gmall.cart.controller;

import com.atguigu.gmall.cart.entity.Cart;
import com.atguigu.gmall.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Cerosg
 * @create 2020年08月11日 17时36分
 */
@Controller
public class CartController {
    @Autowired
    private CartService cartService;

    /**
     * 查询购物车列表
     *
     * @param model
     * @return
     */
    @GetMapping("cart.html")
    public String queryCarts(Model model) {
        List<Cart> carts = cartService.queryCarts();
        model.addAttribute("carts", carts);
        return "cart";
    }

    /**
     * 跳转到向购物车添加商品成功之后的页面
     *
     * @param skuId
     * @param model
     * @return
     */
    @GetMapping("/addCart")
    public String queryCartBySkuId(@RequestParam("skuId") Long skuId, Model model) {
        Cart cart = cartService.queryCartBySkuId(skuId);
        model.addAttribute("cart", cart);
        return "addCart";
    }

    /**
     * 向购物车中添加商品
     *
     * @param cart 购物车
     * @return 重定向至添加成功页
     */
    @GetMapping
    public String addCart(Cart cart) {
        if (cart == null || cart.getSkuId() == null) {
            throw new RuntimeException("您没有选中任何商品！");
        }
        cartService.addCart(cart);
        return "redirect:http://cart.gmall.com/addCart?skuId=" + cart.getSkuId();
    }
}
