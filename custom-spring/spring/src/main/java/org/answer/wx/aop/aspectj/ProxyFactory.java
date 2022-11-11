/**
 * @projectName custom-spring
 * @package org.answer.wx.aop.aspectj
 * @className org.answer.wx.aop.aspectj.ProxyFactory
 */
package org.answer.wx.aop.aspectj;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.answer.wx.aop.aspectj.proxy.Proxy;
import org.answer.wx.aop.aspectj.proxy.ProxyChain;

import java.lang.reflect.Method;
import java.util.List;

/**
 * ProxyFactory
 * @description 代理工厂类
 * @author answer_wx
 * @date 2022/11/3 15:52
 * @version 1.0
 */
public class ProxyFactory {
    public static <T, V> V getInstance(V targetObj, List<Proxy> proxyList) {
        return (V) Enhancer.create(targetObj.getClass(), new MethodInterceptor() {
            /**
             * 代理方法，每次调用目标方法都会先创建一个ProxyChain对象，然后调用该对象的doProxyChain方法
             * @param targetObject
             * @param targetMethod
             * @param methodParams
             * @param methodProxy
             * @return
             * @throws Throwable
             */
            @Override
            public Object intercept(Object targetObject, Method targetMethod, Object[] methodParams, MethodProxy methodProxy) throws Throwable {
                return new ProxyChain(targetObj, targetMethod, methodProxy, methodParams, proxyList).doProxyChain();
            }
        });
    }
}