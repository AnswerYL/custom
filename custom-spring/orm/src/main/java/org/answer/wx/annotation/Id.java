/**
 * @projectName custom-spring
 * @package org.answer.wx.annotation
 * @className org.answer.wx.annotation.Id
 */
package org.answer.wx.annotation;

import org.answer.wx.enums.IdType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Id
 * @description 主键
 * @author answer_wx
 * @date 2022/11/3 15:14
 * @version 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Id {
    // 主键生成规则
    IdType value() default IdType.NONE;
}