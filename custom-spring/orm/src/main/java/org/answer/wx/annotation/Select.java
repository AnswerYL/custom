/**
 * @projectName custom-spring
 * @package org.answer.wx.annotation
 * @className org.answer.wx.annotation.Select
 */
package org.answer.wx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Select
 * @description 查询
 * @author answer_wx
 * @date 2022/11/3 15:24
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Select {
    String value();
}