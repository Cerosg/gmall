package com.atguigu.gmall.wms.api;

import com.atguigu.gmall.common.bean.ResponseVo;
import com.atguigu.gmall.wms.entity.WareSkuEntity;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author Cerosg
 * @describable
 * @create 2020年07月31日 14时42分
 */
public interface GmallWmsApi {
    @ApiOperation("根据skuId在wms中查询销量和库存")
    @GetMapping("wms/waresku/sku/{skuId}")
    ResponseVo<List<WareSkuEntity>> queryWareSkuBySkuId(@PathVariable("skuId") Long skuId);
}
