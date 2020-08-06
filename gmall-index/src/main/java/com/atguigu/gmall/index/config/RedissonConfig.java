package com.atguigu.gmall.index.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月05日 21时26分
 */
@Configuration
public class RedissonConfig {
    @Bean
    public RedissonClient getRedissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://121.196.26.42:6379").setPassword("cerosg");
        return Redisson.create(config);
    }
}
