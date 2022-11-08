/**
 * @projectName custom-spring
 * @package org.answer.wx.annotation
 * @className org.answer.wx.annotation.Insert
 */
package org.answer.wx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Insert
 * @description 插入
 * @author answer_wx
 * @date 2022/11/3 15:21
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Insert {
    String value();
}