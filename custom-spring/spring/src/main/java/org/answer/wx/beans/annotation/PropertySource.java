/**
 * @projectName custom-spring
 * @package org.answer.wx.beans.annotation
 * @className org.answer.wx.beans.annotation.PropertySource
 */
package org.answer.wx.beans.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * PropertySource
 * @description 配置资源
 * @author answer_wx
 * @date 2022/11/3 15:35
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertySource {
    String value();
}