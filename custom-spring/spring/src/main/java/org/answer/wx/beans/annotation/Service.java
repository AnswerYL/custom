/**
 * @projectName custom-spring
 * @package org.answer.wx.beans.annotation
 * @className org.answer.wx.beans.annotation.Service
 */
package org.answer.wx.beans.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Service
 * @description 服务Bean
 * @author answer_wx
 * @date 2022/11/3 15:37
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Service {
}