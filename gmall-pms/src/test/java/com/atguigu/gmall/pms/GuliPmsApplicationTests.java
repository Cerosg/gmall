package com.atguigu.gmall.pms;

import com.atguigu.gmall.pms.service.SkuAttrValueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GuliPmsApplicationTests {
    @Autowired
    private SkuAttrValueService skuAttrValueService;

    @Test
    void test01() {
        String s = skuAttrValueService.querySaleAttrMappingBySpuId(7L);
        System.out.println(s);
    }
}
