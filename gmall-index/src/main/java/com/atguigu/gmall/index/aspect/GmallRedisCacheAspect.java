package com.atguigu.gmall.index.aspect;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月06日 13时58分
 */
@Aspect
@Component
public class GmallRedisCacheAspect {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 获取目标对象类：joinPoint.getTarget().getClass();<br/>
     * 获取目标方法参数：joinPoint.getArgs();<br/>
     * 获取目标方法：<br/>
     * MethodSignature signature = (MethodSignature) joinPoint.getSignature();<br/>
     * Method method = signature.getMethod();<br/>
     *
     * @param joinPoint 切入点
     * @return 目标方法的返回值
     * @throws Throwable
     */
    @Around("@annotation(GmallRedisCache)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();// 获取方法签名
        Method method = signature.getMethod();// 获取目标方法
        GmallRedisCache gmallRedisCache = method.getAnnotation(GmallRedisCache.class);// 获取目标方法的注解
        String prefix = gmallRedisCache.prefix();// 获取目标方法注解的属性
        List<Object> args = Arrays.asList(joinPoint.getArgs());// 获取目标方法的形参列表并将其转为集合
        String key = prefix + args;// 拼接缓存key=注解的prefix属性值+方法形参
        Class<?> returnType = signature.getReturnType();// 获取方法的返回值类型

        // 1.查询缓存，如果命中，直接返回
        String cacheJson = redisTemplate.opsForValue().get(key);
        if (cacheJson != null) {
            return JSON.parseObject(cacheJson, returnType);
        }
        // 2.加分布式锁
        String lock = gmallRedisCache.lock();// 获取目标方法注解的lock属性值
        RLock fairLock = redissonClient.getFairLock(lock + args);
        fairLock.lock();
        // 3.再查缓存，如果命中，直接返回
        String cacheJson2 = redisTemplate.opsForValue().get(key);
        if (cacheJson2 != null) {
            fairLock.unlock();
            return JSON.parseObject(cacheJson2, returnType);
        }
        // 4.执行目标方法，即从数据库中查询所需数据
        Object result = joinPoint.proceed(joinPoint.getArgs());
        // 5.将目标结果的结果集放入缓存
        int timeout = gmallRedisCache.timeout();
        int random = gmallRedisCache.random();
        redisTemplate.opsForValue().set(key, JSON.toJSONString(result), timeout + new Random().nextInt(random), TimeUnit.MINUTES);
        // 6.释放分布式锁
        fairLock.unlock();
        return result;
    }
}
