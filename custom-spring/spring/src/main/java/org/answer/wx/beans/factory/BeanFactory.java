/**
 * @projectName custom-spring
 * @package org.answer.wx.beans.factory
 * @className org.answer.wx.beans.factory.BeanFactory
 */
package org.answer.wx.beans.factory;

/**
 * BeanFactory
 * @description Bean工厂
 * @author answer_wx
 * @date 2022/11/3 15:40
 * @version 1.0
 */
public interface BeanFactory {
    Object getBean(String name);

    <T> T getBean(String name, Class<T> requiredType);

    Object getBean(String name, Object... args);

    <T> T getBean(Class<T> requiredType);

    <T> T getBean(Class<T> requiredType, Object... args);

    boolean containsBean(String name);

    boolean isSingleton(String name);

    boolean isPrototype(String name);

    boolean isTypeMatch(String name, Class<?> typeToMatch);

    Class<?> getType(String name);

    String[] getAliases(String name);

}