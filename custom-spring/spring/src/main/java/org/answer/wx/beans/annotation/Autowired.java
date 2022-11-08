/**
 * @projectName custom-spring
 * @package org.answer.wx.beans.annotation
 * @className org.answer.wx.beans.annotation.Autowired
 */
package org.answer.wx.beans.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Autowired
 * @description 依赖注入
 * @author answer_wx
 * @date 2022/11/3 15:31
 * @version 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
}