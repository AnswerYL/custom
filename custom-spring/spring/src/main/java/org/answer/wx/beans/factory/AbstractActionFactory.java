/**
 * @projectName custom-spring
 * @package org.answer.wx.beans.factory
 * @className org.answer.wx.beans.factory.AbstractActionFactory
 */
package org.answer.wx.beans.factory;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AbstractActionFactory
 * @description 抽象bean工厂
 * @author answer_wx
 * @date 2022/11/3 15:43
 * @version 1.0
 */
public abstract class AbstractActionFactory {
    /**
     * 存放bean容器
     */
    protected static Map<String, Object> beanMap = new ConcurrentHashMap<>();

    /**
     * 扫描并加载组件
     *
     * @param clazz
     * @param properties
     */
    protected abstract void scanComponent(Class clazz, Properties properties);

    /**
     * 装配组件
     * @param beanFactory
     */
    protected abstract void autowiredComponent(BeanFactory beanFactory);

    /**
     * 加载切面的组件
     */
    protected abstract void loadAspect();

}