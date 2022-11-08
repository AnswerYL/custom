/**
 * @projectName custom-spring
 * @package org.answer.wx.aop.aspectj.proxy
 * @className org.answer.wx.aop.aspectj.proxy.Proxy
 */
package org.answer.wx.aop.aspectj.proxy;

/**
 * Proxy
 * @description 代理接口
 * @author answer_wx
 * @date 2022/11/3 15:53
 * @version 1.0
 */
public interface Proxy {
    Object doProxy(ProxyChain proxyChain) throws Throwable;
}