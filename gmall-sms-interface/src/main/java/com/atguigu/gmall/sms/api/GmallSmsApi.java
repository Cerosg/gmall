package com.atguigu.gmall.sms.api;

import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.sms.vo.SkuSaleVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Cerosg
 * @describable
 * @create 2020年07月23日 17时17分
 */
public interface GmallSmsApi {
    @PostMapping("/sms/skubounds/sku/sales")
    ResponseVo<Object> saveSkuSaleAttrs(@RequestBody SkuSaleVo skuSaleVo);
}
