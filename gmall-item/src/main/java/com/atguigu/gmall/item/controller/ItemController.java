package com.atguigu.gmall.item.controller;

import com.atguigu.gmall.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月07日 20时41分
 */
@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;

    @GetMapping("/{skuId}.html")
//    @ResponseBody
    public String loadData(@PathVariable("skuId") Long skuId, Model model) {
        model.addAttribute("itemVo", itemService.loadData(skuId));
        return "item";
    }
}
