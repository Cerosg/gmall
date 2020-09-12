package com.atguigu.gmall.cart.feign;

import com.atguigu.gmall.sms.api.GmallSmsApi;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Cerosg
 * @create 2020年08月11日 17时42分
 */
@FeignClient("sms-service")
public interface GmallSmsClient extends GmallSmsApi {
}
