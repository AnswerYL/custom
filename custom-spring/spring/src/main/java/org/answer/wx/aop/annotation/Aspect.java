/**
 * @projectName custom-spring
 * @package org.answer.wx.aop.annotation
 * @className org.answer.wx.aop.annotation.Aspect
 */
package org.answer.wx.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Aspect
 * @description 切面
 * @author answer_wx
 * @date 2022/11/3 15:48
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {
    String packageName() default "";

    String className() default "";
}