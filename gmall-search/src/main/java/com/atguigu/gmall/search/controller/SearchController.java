package com.atguigu.gmall.search.controller;

import com.atguigu.gmall.search.pojo.SearchParamVo;
import com.atguigu.gmall.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月01日 14时50分
 */
@Controller
public class SearchController {
    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
    public String search(SearchParamVo searchParamVo, Model model) {
        model.addAttribute("response", searchService.search(searchParamVo));
        model.addAttribute("searchParam", searchParamVo);
        return "search";
    }
}
