/**
 * @projectName custom-spring
 * @package org.answer.wx.aop.aspectj.proxy
 * @className org.answer.wx.aop.aspectj.proxy.AspectProxy
 */
package org.answer.wx.aop.aspectj.proxy;

import org.answer.wx.aop.annotation.After;
import org.answer.wx.aop.annotation.Around;
import org.answer.wx.aop.annotation.Before;
import org.answer.wx.aop.annotation.Throwing;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * AspectProxy
 * @description 切面代理
 * @author answer_wx
 * @date 2022/11/3 16:07
 * @version 1.0
 */
public class AspectProxy implements Proxy {

    private Object instance;

    public AspectProxy(Object instance) {
        this.instance = instance;
    }

    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result;
        Method method = proxyChain.getTargetMethod();
        Object[] params = proxyChain.getMethodParams();

        try {
            if (intercept(method, params)) {
                before(params);
            }
            result = around(method, params, proxyChain);
            if (intercept(method, params)) {
                after(params);
            }
        } catch (Exception e) {
            if (intercept(method, params)) {
                throwing(params, e);
            }
            throw e;
        }
        return result;
    }

    public boolean intercept(Method method, Object[] params) throws Throwable {
        return true;
    }

    private void before(Object[] params) {
        Method[] methods = instance.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Before.class)) {
                try {
                    method.setAccessible(true);
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length == 0) {
                        method.invoke(instance);
                    } else {
                        method.invoke(instance, (Object) params);
                    }
                } catch (InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void after(Object[] params) {
        Method[] methods = instance.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(After.class)) {
                try {
                    method.setAccessible(true);
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length == 0) {
                        method.invoke(instance);
                    } else {
                        method.invoke(instance, (Object) params);
                    }
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Object around(Method meth, Object[] params, ProxyChain proxyChain) throws Throwable {
        Object ret;
        if (!intercept(meth, params)) {
            return proxyChain.doProxyChain();
        }
        Method[] methods = instance.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Around.class)) {
                try {
                    method.setAccessible(true);
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length == 0) {
                        ret = method.invoke(instance);
                    } else {
                        ret = method.invoke(instance, (Object) params, proxyChain);
                    }
                    return ret;
                } catch (Exception e) {
                    throw e;
                }
            }
        }

        return proxyChain.doProxyChain();
    }

    private void throwing(Object[] params, Exception ex) {

        Method[] methods = instance.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Throwing.class)) {
                try {
                    method.setAccessible(true);
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length == 0) {
                        method.invoke(instance);
                    } else {
                        method.invoke(instance, (Object) params, ex);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}