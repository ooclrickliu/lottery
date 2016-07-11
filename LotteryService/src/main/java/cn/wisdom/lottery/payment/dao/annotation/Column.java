/**
 * Column.java
 * 
 * Copyright@2016 OVT Inc. All rights reserved. 
 * 
 * 2016年7月5日
 */
package cn.wisdom.lottery.payment.dao.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Column
 * 
 * @Author brad.zhou
 * @Version 1.0
 * @See
 * @Since [OVT Cloud Platform]/[API] 1.0
 */

// 注解会在class字节码文件中存在，运行时可通过反射获得
@Retention(RetentionPolicy.RUNTIME)
// 定义注解的作用目标——属性和方法的声明
@Target({ ElementType.FIELD, ElementType.METHOD })
// 说明该注解被包含在javadoc中
@Documented
public @interface Column
{
    /**
     * 字段名称
     * 
     * @return
     */
    String value() default "";
}
