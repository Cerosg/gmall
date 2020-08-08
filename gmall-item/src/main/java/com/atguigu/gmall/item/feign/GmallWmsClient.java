package com.atguigu.gmall.item.feign;

import com.atguigu.gmall.wms.api.GmallWmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Cerosg
 * @describable 远程调用pms的接口
 * @create 2020年08月06日 17时17分
 */
@FeignClient("wms-service")
public interface GmallWmsClient extends GmallWmsApi {
}
