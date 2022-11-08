/**
 * @projectName custom-spring
 * @package org.answer.wx.context
 * @className org.answer.wx.context.ApplicationContext
 */
package org.answer.wx.context;

import org.answer.wx.beans.factory.BeanFactory;

import java.util.Map;

/**
 * ApplicationContext
 * @description 应用容器接口
 * @author answer_wx
 * @date 2022/11/4 16:00
 * @version 1.0
 */
public interface ApplicationContext extends BeanFactory {
    /**
     * 注册一个bean
     */
    boolean register(String name, Object bean);

    /**
     * 根据name删除一个bean
     * @param name
     * @return
     */
    boolean remove(String name);

    /**
     * 根据类型删除一个bean
     * @param requiredType
     * @return
     */
    boolean remove(Class<?> requiredType);

    /**
     * 获取IOC容器
     * @return
     */
    Map<String, Object> getIOC();
}