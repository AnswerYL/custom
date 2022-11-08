/**
 * @projectName custom-spring
 * @package org.answer.wx.beans.annotation
 * @className org.answer.wx.beans.annotation.Bean
 */
package org.answer.wx.beans.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Bean
 * @description 生成bean
 * @author answer_wx
 * @date 2022/11/3 15:32
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Bean {
}