/**
 * @projectName custom-spring
 * @package org.answer.wx.aop.aspectj.proxy
 * @className org.answer.wx.aop.aspectj.proxy.ProxyChain
 */
package org.answer.wx.aop.aspectj.proxy;

import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * ProxyChain
 * @description 代理链
 * @author answer_wx
 * @date 2022/11/3 15:54
 * @version 1.0
 */
public class ProxyChain {
    /**
     * 目标对象
     */
    private final Object targetObject;
    /**
     * 目标方法
     */
    private final Method targetMethod;
    /**
     * 方法代理
     */
    private final MethodProxy methodProxy;
    /**
     * 方法参数
     */
    private final Object[] methodParams;

    /**
     * 代理列表
     */
    private List<Proxy> proxyList = new ArrayList<>();

    /**
     * 代理索引
     * 并发情况下放到threadLocal or concurrentMap 线程号为key
     */
    private int proxyIndex = 0;

    public ProxyChain(Object targetObject, Method targetMethod, MethodProxy methodProxy, Object[] methodParams, List<Proxy> proxyList) {
        this.targetObject = targetObject;
        this.targetMethod = targetMethod;
        this.methodProxy = methodProxy;
        this.methodParams = methodParams;
        this.proxyList = proxyList;
    }

    public Method getTargetMethod() {
        return targetMethod;
    }

    public Object[] getMethodParams() {
        return methodParams;
    }

    public Class<?> getTargetClass() {
        return targetObject.getClass();
    }

    /**
     * 递归执行
     */
    public Object doProxyChain() throws Throwable {
        Object methodResult;
        if (proxyIndex < proxyList.size()) {
            // 执行增强方法
            methodResult = proxyList.get(proxyIndex++).doProxy(this);
        } else {
            // 目标方法最后执行且只执行一次
            methodResult = targetMethod.invoke(targetObject, methodParams);
        }
        return methodResult;
    }
}