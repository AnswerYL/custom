/**
 * @projectName custom-spring
 * @package org.answer.wx.handler
 * @className org.answer.wx.handler.HandlerMethod
 */
package org.answer.wx.handler;

import java.lang.reflect.Method;

/**
 * HandlerMethod
 * @description 处理器方法
 * @author answer_wx
 * @date 2022/11/8 14:42
 * @version 1.0
 */
public class HandlerMethod {
    protected final Object bean;
    protected final Method method;

    public HandlerMethod(Object bean, Method method) {
        this.bean = bean;
        this.method = method;
    }
}