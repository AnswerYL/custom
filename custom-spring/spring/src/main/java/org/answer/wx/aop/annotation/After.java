/**
 * @projectName custom-spring
 * @package org.answer.wx.aop.annotation
 * @className org.answer.wx.aop.annotation.After
 */
package org.answer.wx.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * After
 * @description 切面-后
 * @author answer_wx
 * @date 2022/11/3 15:46
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface After {
}