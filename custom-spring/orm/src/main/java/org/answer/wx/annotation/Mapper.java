/**
 * @projectName custom-spring
 * @package org.answer.wx.annotation
 * @className org.answer.wx.annotation.Mapper
 */
package org.answer.wx.annotation;

import java.lang.annotation.*;

/**
 * Mapper
 * @description 指定Mapper
 * @author answer_wx
 * @date 2022/11/3 15:25
 * @version 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Mapper {
}