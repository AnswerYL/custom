/**
 * @projectName custom-spring
 * @package org.answer.wx.aop.annotation
 * @className org.answer.wx.aop.annotation.Throwing
 */
package org.answer.wx.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Throwing
 * @description 切面过程中异常处理
 * @author answer_wx
 * @date 2022/11/3 15:50
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Throwing {
}