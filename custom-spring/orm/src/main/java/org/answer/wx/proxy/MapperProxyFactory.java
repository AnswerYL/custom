/**
 * @projectName custom-spring
 * @package org.answer.wx.proxy
 * @className org.answer.wx.proxy.MapperProxyFactory
 */
package org.answer.wx.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * MapperProxyFactory
 * @description
 * @author answer_wx
 * @date 2022/11/4 17:14
 * @version 1.0
 */
public class MapperProxyFactory {
    public static <T> T getProxy(Class clazz) {
        return (T) Enhancer.create(clazz, new MethodInterceptor() {
            @Override
            public Object intercept(Object targetObject, Method targetMethod, Object[] methodParams, MethodProxy methodProxy) throws Throwable {
                return AnalyticMethod.invoke(clazz, targetObject, targetMethod, methodParams);
            }
        });
    }
}