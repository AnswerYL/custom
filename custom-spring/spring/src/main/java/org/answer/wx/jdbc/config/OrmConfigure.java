/**
 * @projectName custom-spring
 * @package org.answer.wx.config
 * @className org.answer.wx.config.OrmConfig
 */
package org.answer.wx.jdbc.config;


import org.answer.wx.context.ApplicationContext;

import java.lang.annotation.Annotation;
import java.util.Properties;

/**
 * OrmConfig
 * @description orm配置
 * @author answer_wx
 * @date 2022/11/4 16:47
 * @version 1.0
 */
public interface OrmConfigure {
    void auto(ApplicationContext context, Annotation annotation, Properties prop);
}