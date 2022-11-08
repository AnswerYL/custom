/**
 * @projectName custom-spring
 * @package org.answer.wx.beans.annotation
 * @className org.answer.wx.beans.annotation.ConfigurationProperties
 */
package org.answer.wx.beans.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ConfigurationProperties
 * @description 自定义配置
 * @author answer_wx
 * @date 2022/11/8 21:03
 * @version 1.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigurationProperties {
    String prefix() default "";
}