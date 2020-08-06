package com.atguigu.gmall.index;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月06日 15时39分
 */
public class ObjectTest {
    @Test
    public void arraysTest() {
        Object[] objects = new Object[]{1, 2, 3, 4, 5, 66};
        objects[2] = 5;
        System.out.println(Arrays.asList(objects));// [1, 2, 5, 4, 5, 66]
        System.out.println(Arrays.toString(objects));// [1, 2, 5, 4, 5, 66]
    }
}
