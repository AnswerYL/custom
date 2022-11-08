/**
 * @projectName custom-spring
 * @package org.answer.wx.aop
 * @className org.answer.wx.aop.SpringBootApplication
 */
package org.answer.wx.web.annotation;

import java.lang.annotation.*;

/**
 * SpringBootApplication
 * @description SpringBootApplication 注解
 * @author answer_wx
 * @date 2022/11/7 21:34
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SpringBootApplication {
    String[] scanBasePackages() default {};
}