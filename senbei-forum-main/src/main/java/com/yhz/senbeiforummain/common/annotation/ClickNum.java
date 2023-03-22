package com.yhz.senbeiforummain.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于统计访问量
 * @author yanhuanzhan
 * @date 2023/3/20 - 15:40
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface ClickNum {
    String type() default "";
}
