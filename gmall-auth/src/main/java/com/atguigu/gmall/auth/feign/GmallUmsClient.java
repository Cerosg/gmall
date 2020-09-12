package com.atguigu.gmall.auth.feign;

import com.atguigu.gmall.ums.api.GmallUmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月10日 18时54分
 */
@FeignClient("ums-service")
public interface GmallUmsClient extends GmallUmsApi {
}
