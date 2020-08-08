package com.atguigu.gmall.item.feign;

import com.atguigu.gmall.pms.api.GmallPmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Cerosg
 * @describable 远程调用pms的接口
 * @create 2020年08月06日 17时17分
 */
@FeignClient("pms-service")
public interface GmallPmsClient extends GmallPmsApi {
}
