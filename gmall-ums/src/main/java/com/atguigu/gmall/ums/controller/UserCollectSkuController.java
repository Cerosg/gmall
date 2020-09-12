package com.atguigu.gmall.ums.controller;

import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atguigu.gmall.ums.entity.UserCollectSkuEntity;
import com.atguigu.gmall.ums.service.UserCollectSkuService;
import com.atguigu.gmall.common.bean.PageResultVo;
import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.common.bean.PageParamVo;

/**
 * 关注商品表
 *
 * @author Cerosg
 * @email Cerosg@outlook.com
 * @date 2020-07-20 20:25:53
 */
@Api(tags = "关注商品表 管理")
@RestController
@RequestMapping("ums/usercollectsku")
public class UserCollectSkuController {

    @Autowired
    private UserCollectSkuService userCollectSkuService;

    /**
     * 列表
     */
    @GetMapping
    @ApiOperation("分页查询")
    public ResponseVo<PageResultVo> queryUserCollectSkuByPage(PageParamVo paramVo){
        PageResultVo pageResultVo = userCollectSkuService.queryPage(paramVo);

        return ResponseVo.ok(pageResultVo);
    }


    /**
     * 信息
     */
    @GetMapping("{id}")
    @ApiOperation("详情查询")
    public ResponseVo<UserCollectSkuEntity> queryUserCollectSkuById(@PathVariable("id") Long id){
		UserCollectSkuEntity userCollectSku = userCollectSkuService.getById(id);

        return ResponseVo.ok(userCollectSku);
    }

    /**
     * 保存
     */
    @PostMapping
    @ApiOperation("保存")
    public ResponseVo<Object> save(@RequestBody UserCollectSkuEntity userCollectSku){
		userCollectSkuService.save(userCollectSku);

        return ResponseVo.ok();
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    @ApiOperation("修改")
    public ResponseVo update(@RequestBody UserCollectSkuEntity userCollectSku){
		userCollectSkuService.updateById(userCollectSku);

        return ResponseVo.ok();
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    @ApiOperation("删除")
    public ResponseVo delete(@RequestBody List<Long> ids){
		userCollectSkuService.removeByIds(ids);

        return ResponseVo.ok();
    }

}
