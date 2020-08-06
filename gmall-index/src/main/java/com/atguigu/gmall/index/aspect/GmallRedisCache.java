package com.atguigu.gmall.index.aspect;

import java.lang.annotation.*;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月06日 13时36分
 */
// 使用位置（方法上、类上等）
@Target({ElementType.METHOD})
// 编译时注解/运行时注解
@Retention(RetentionPolicy.RUNTIME)
// 添加到文档
@Documented
public @interface GmallRedisCache {
    /**
     * 该属性可以设置缓存key的前缀
     *
     * @return
     */
    String prefix() default "";

    /**
     * 该属性可以指定分布式锁的名称
     *
     * @return
     */
    String lock() default "lock";

    /**
     * 该属性指定缓存的过期时间<br/>
     * 单位：分钟
     *
     * @return
     */
    int timeout() default 5;

    /**
     * 防止缓存雪崩，为缓存过期时间添加随机值<br/>
     * 该属性设置过期时间的随机值<br/>
     * 单位：分钟
     *
     * @return
     */
    int random() default 5;
}
