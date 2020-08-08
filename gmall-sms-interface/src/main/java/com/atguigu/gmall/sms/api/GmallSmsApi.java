package com.atguigu.gmall.sms.api;

import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.sms.vo.ItemSaleVo;
import com.atguigu.gmall.sms.vo.SkuSaleVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Cerosg
 * @describable
 * @create 2020年07月23日 17时17分
 */
public interface GmallSmsApi {
    @PostMapping("/sms/skubounds/sku/sales")
    ResponseVo<Object> saveSkuSaleAttrs(@RequestBody SkuSaleVo skuSaleVo);

    @GetMapping("/sms/skubounds/sku/{skuId}")
    ResponseVo<List<ItemSaleVo>> querySalesBySkuId(@PathVariable("skuId") Long skuId);
}
