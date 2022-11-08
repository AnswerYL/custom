/**
 * @projectName custom-spring
 * @package org.answer.wx.context
 * @className org.answer.wx.context.AnnotationConfigApplicationContext
 */
package org.answer.wx.context;

import java.util.Properties;

/**
 * AnnotationConfigApplicationContext
 * @description
 * @author answer_wx
 * @date 2022/11/4 15:58
 * @version 1.0
 */
public class AnnotationConfigApplicationContext extends GenericApplicationContext {

    public AnnotationConfigApplicationContext(Class<?> clazz, Properties prop) {
        GenericApplicationContext context = new GenericApplicationContext();
        context.loadApplicationContext(clazz, prop);
    }
}