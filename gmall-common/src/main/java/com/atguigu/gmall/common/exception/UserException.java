package com.atguigu.gmall.common.exception;

/**
 * @author Cerosg
 * @describable
 * @create 2020年08月10日 19时02分
 */

/**
 * 自定义用户异常类
 */
public class UserException extends RuntimeException {
    public UserException() {
    }

    public UserException(String message) {
        super(message);
    }
}
