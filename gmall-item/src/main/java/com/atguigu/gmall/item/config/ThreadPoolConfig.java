package com.atguigu.gmall.item.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月08日 09时45分
 */
@Configuration
public class ThreadPoolConfig {
    @Bean
    public ThreadPoolExecutor threadPoolExecutor(
            @Value("${threadPool.coreSize}") Integer coreSize,
            @Value("${threadPool.maxSize}") Integer maxSize,
            @Value("${threadPool.timeout}") Long timeout,
            @Value("${threadPool.blockingSize}") Integer blockingSize) {
        return new ThreadPoolExecutor(coreSize, maxSize, timeout, TimeUnit.MINUTES, new ArrayBlockingQueue<>(blockingSize));
    }
}
