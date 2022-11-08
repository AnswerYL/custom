/**
 * @projectName custom-spring
 * @package org.answer.wx.transaction
 * @className org.answer.wx.transaction.TransactionProxy
 */
package org.answer.wx.transaction;

import org.answer.wx.aop.aspectj.proxy.Proxy;
import org.answer.wx.aop.aspectj.proxy.ProxyChain;
import org.answer.wx.beans.factory.BeanFactory;

import java.lang.reflect.Method;

/**
 * TransactionProxy
 * @description 事务代理
 * @author answer_wx
 * @date 2022/11/3 18:11
 * @version 1.0
 */
public class TransactionProxy implements Proxy {

    private BeanFactory beanFactory;

    public TransactionProxy(){}

    public TransactionProxy(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result = null;
        Method method = proxyChain.getTargetMethod();
        return result;
    }
}