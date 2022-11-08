/**
 * @projectName custom-spring
 * @package org.answer.wx.web.annotation
 * @className org.answer.wx.web.annotation.Controller
 */
package org.answer.wx.web.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Controller
 * @description 控制器注解
 * @author answer_wx
 * @date 2022/11/3 17:22
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Controller {
}