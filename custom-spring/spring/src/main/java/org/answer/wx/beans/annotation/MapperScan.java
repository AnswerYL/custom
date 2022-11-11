/**
 * @projectName custom-spring
 * @package org.answer.wx.annotation
 * @className org.answer.wx.annotation.MapperScan
 */
package org.answer.wx.beans.annotation;

import org.answer.wx.jdbc.config.MybatisConfig;

import java.lang.annotation.*;

/**
 * MapperScan
 * @description Mapper文件扫描
 * @author answer_wx
 * @date 2022/11/3 15:26
 * @version 1.0
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(MybatisConfig.class)
public @interface MapperScan {
    String[] basePackages() default {};
}