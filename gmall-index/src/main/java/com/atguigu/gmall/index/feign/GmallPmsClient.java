package com.atguigu.gmall.index.feign;

import com.atguigu.gmall.pms.api.GmallPmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月05日 09时49分
 */
@FeignClient("pms-service")
public interface GmallPmsClient extends GmallPmsApi {
}
