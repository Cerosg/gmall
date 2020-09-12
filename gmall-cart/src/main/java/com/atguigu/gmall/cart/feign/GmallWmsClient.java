package com.atguigu.gmall.cart.feign;

import com.atguigu.gmall.wms.api.GmallWmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Cerosg
 * @create 2020年08月11日 17时43分
 */
@FeignClient("wms-service")
public interface GmallWmsClient extends GmallWmsApi {
}
