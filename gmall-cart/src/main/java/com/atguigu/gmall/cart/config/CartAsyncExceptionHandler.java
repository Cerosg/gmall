package com.atguigu.gmall.cart.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 异常处理类：处理异步存入MySQL数据库时可能出现的异常
 * @author Cerosg
 * @create 2020年08月17日 20时46分
 */
@Component
@Slf4j
public class CartAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {
    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        log.error("有一个子任务出现了异常，异常信息：{}，异常方法：{}，异常方法：{}，方法参数：{}", ex.getMessage(), method, params);
    }
}
