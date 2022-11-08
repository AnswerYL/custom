/**
 * @projectName custom-spring
 * @package org.answer.wx.aop.annotation
 * @className org.answer.wx.aop.annotation.EnableAspectJAutoProxy
 */
package org.answer.wx.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * EnableAspectJAutoProxy
 * @description 开启切面代理
 * @author answer_wx
 * @date 2022/11/3 15:50
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableAspectJAutoProxy {
}