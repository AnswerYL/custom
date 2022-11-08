/**
 * @projectName custom-spring
 * @package org.answer.wx.annotation
 * @className org.answer.wx.annotation.Column
 */
package org.answer.wx.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Column
 * @description 列
 * @author answer_wx
 * @date 2022/11/3 15:08
 * @version 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {
    String value() default "";

    String type() default "";

    boolean unique() default false;

    boolean index() default false;

    boolean nullable() default false;

    int length() default -1;
}