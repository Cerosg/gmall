package com.atguigu.gmall.search.feign;

import com.atguigu.gmall.wms.api.GmallWmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Cerosg
 * @describable
 * @create 2020年07月31日 16时41分
 */
@FeignClient("wms-service")
public interface GmallWmsClient extends GmallWmsApi {
}
