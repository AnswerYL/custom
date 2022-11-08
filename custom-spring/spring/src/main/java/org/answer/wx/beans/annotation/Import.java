/**
 * @projectName custom-spring
 * @package org.answer.wx.beans.annotation
 * @className org.answer.wx.beans.annotation.Import
 */
package org.answer.wx.beans.annotation;

import java.lang.annotation.*;

/**
 * Import
 * @description spring 注解
 * @author answer_wx
 * @date 2022/11/3 15:30
 * @version 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Import {
    Class<?>[] value() default {};
}