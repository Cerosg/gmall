package com.atguigu.gmall.cart.feign;

import com.atguigu.gmall.pms.api.GmallPmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Cerosg
 * @create 2020年08月11日 17时41分
 */
@FeignClient("pms-service")
public interface GmallPmsClient extends GmallPmsApi {
}
