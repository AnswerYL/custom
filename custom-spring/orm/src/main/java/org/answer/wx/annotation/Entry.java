/**
 * @projectName custom-spring
 * @package org.answer.wx.annotation
 * @className org.answer.wx.annotation.Entry
 */
package org.answer.wx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Entry
 * @description 实体
 * @author answer_wx
 * @date 2022/11/3 15:13
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Entry {
}