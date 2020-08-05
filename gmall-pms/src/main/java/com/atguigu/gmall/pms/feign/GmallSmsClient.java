package com.atguigu.gmall.pms.feign;

import com.atguigu.gmall.sms.api.GmallSmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Cerosg
 * @describable
 * @create 2020年07月23日 17时26分
 */
@FeignClient("sms-service")
public interface GmallSmsClient extends GmallSmsApi {
}
