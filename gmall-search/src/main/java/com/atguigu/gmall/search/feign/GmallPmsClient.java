package com.atguigu.gmall.search.feign;

import com.atguigu.gmall.pms.api.GmallPmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Cerosg
 * @describable
 * @create 2020年07月31日 15时58分
 */
@FeignClient("pms-service")
public interface GmallPmsClient extends GmallPmsApi {
}
