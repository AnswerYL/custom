/**
 * @projectName custom-spring
 * @package org.answer.wx.web.annotation
 * @className org.answer.wx.web.annotation.RequestMapping
 */
package org.answer.wx.web.annotation;


import org.answer.wx.web.enums.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RequestMapping
 * @description
 * @author answer_wx
 * @date 2022/11/8 14:51
 * @version 1.0
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    RequestMethod[] method() default {};

    String value() default "";
}