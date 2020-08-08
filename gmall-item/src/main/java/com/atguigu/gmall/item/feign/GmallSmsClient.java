package com.atguigu.gmall.item.feign;

import com.atguigu.gmall.sms.api.GmallSmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Cerosg
 * @describable 远程调用pms的接口
 * @create 2020年08月06日 17时17分
 */
@FeignClient("sms-service")
public interface GmallSmsClient extends GmallSmsApi {
}
