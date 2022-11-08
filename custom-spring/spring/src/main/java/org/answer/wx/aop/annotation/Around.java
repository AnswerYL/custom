/**
 * @projectName custom-spring
 * @package org.answer.wx.aop.annotation
 * @className org.answer.wx.aop.annotation.Around
 */
package org.answer.wx.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Around
 * @description 切面环绕
 * @author answer_wx
 * @date 2022/11/3 15:48
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Around {
}