/**
 * @projectName custom-spring
 * @package org.answer.wx.web.annotation
 * @className org.answer.wx.web.annotation.ResponseBody
 */
package org.answer.wx.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ResponseBody
 * @description 返回注解
 * @author answer_wx
 * @date 2022/11/8 14:54
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseBody {
}