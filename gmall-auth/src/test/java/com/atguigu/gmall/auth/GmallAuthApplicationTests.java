package com.atguigu.gmall.auth;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GmallAuthApplicationTests {

    @Test
    void test01() {
        System.out.println(DigestUtils.md5Hex("123456"));
    }

    @Test
    void contextLoads() {
    }

}
