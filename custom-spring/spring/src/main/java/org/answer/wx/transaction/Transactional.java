/**
 * @projectName custom-spring
 * @package org.answer.wx.transaction
 * @className org.answer.wx.transaction.Transactional
 */
package org.answer.wx.transaction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Transactional
 * @description 事务注解
 * @author answer_wx
 * @date 2022/11/3 18:10
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Transactional {
}