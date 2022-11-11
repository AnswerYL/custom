/**
 * @projectName custom
 * @package com.answer.wx.aspect
 * @className com.answer.wx.aspect.TestAspect
 */
package com.answer.wx.aspect;

import org.answer.wx.aop.annotation.*;
import org.answer.wx.aop.aspectj.proxy.ProxyChain;
import org.answer.wx.beans.annotation.Component;
import org.apache.log4j.Logger;

/**
 * TestAspect
 * @description 测试切面
 * @author answer_wx
 * @date 2022/11/11 15:21
 * @version 1.0
 */
@Component
@Aspect(packageName = "com.answer.wx.service.impl", className = "TestServiceImpl")
public class TestAspect {
    Logger log = Logger.getLogger(TestAspect.class);

    @Before
    public void before(Object[] object) {
        log.info("Before");
    }

    @After
    public void after(Object[] object) {
        log.info("After");
    }

    @Around
    public Object around(Object[] object, ProxyChain proxyChain) throws Throwable {
        Object ret = null;
        log.info("Around before");
        try {
            ret = proxyChain.doProxyChain();
            log.info("Around result " + ret);
        } catch (Throwable throwable) {
            throw throwable;
        }
        log.info("Around after");
        return ret;
    }

    @Throwing
    public void throwing(Object[] object, Exception e) {
        if (e.getClass() == NullPointerException.class) {
            log.error("Throwing NullPointerException");
        }
        log.error("Throwing " + e.getMessage());
    }
}