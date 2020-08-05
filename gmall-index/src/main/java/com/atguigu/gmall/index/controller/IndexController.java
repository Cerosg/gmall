package com.atguigu.gmall.index.controller;

import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.index.service.IndexService;
import com.atguigu.gmall.pms.entity.CategoryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月05日 09时46分
 */
@Controller // 不能使用RestController，否则无法跳转页面
public class IndexController {

    @Autowired
    private IndexService indexService;

    /**
     * 跳转到首页并显示一级分类
     *
     * @param model 视图
     * @return 跳转到index.html页面
     */
    @GetMapping
    public String toIndex(Model model) {
        model.addAttribute("categories", indexService.getCategoryLv1ById());
        // TODO: 2020/8/5 加载首页中的其他数据
        return "index";
    }

    /**
     * 获取某个菜单的子菜单
     */
    @ResponseBody // 以json格式响应请求
    @GetMapping("/index/cates/{pid}")
    public ResponseVo<List<CategoryEntity>> getSubMenuByPid(@PathVariable("pid") Long pid) {
        return ResponseVo.ok(indexService.querySubMenuByPid(pid));
    }
}
