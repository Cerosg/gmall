package com.atguigu.gmall.cart.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;

import java.util.concurrent.Executor;

/**
 *
 * @author Cerosg
 * @create 2020年08月17日 20时50分
 */
@Configuration
public class CartAsyncConfig implements AsyncConfigurer {
    @Autowired
    private CartAsyncExceptionHandler cartAsyncExceptionHandler;

    /**
     * 为SpringTask定义专有的线程池
     *
     * @return
     */
    @Override
    public Executor getAsyncExecutor() {
        return null;
    }

    /**
     * 注册统一异常处理器
     *
     * @return
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return cartAsyncExceptionHandler;
    }
}
