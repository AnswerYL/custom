/**
 * @projectName custom-spring
 * @package org.answer.wx.transaction
 * @className org.answer.wx.transaction.TransactionProxy
 */
package org.answer.wx.transaction;

import org.answer.wx.aop.aspectj.proxy.Proxy;
import org.answer.wx.aop.aspectj.proxy.ProxyChain;
import org.answer.wx.beans.factory.BeanFactory;
import org.answer.wx.jdbc.session.SqlSessionFactory;
import org.answer.wx.proxy.AnalyticMethod;
import org.answer.wx.session.SqlSession;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;

/**
 * TransactionProxy
 * @description 事务代理
 * @author answer_wx
 * @date 2022/11/3 18:11
 * @version 1.0
 */
public class TransactionProxy implements Proxy {

    Logger log = Logger.getLogger(TransactionProxy.class);

    private BeanFactory beanFactory;

    public TransactionProxy() {
    }

    public TransactionProxy(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object doProxy(ProxyChain proxyChain) throws Throwable {
        Object result;
        Method method = proxyChain.getTargetMethod();
        SqlSessionFactory sqlSessionFactory = beanFactory.getBean(SqlSessionFactory.class);
        //加了@Transactional注解的方法要做事务处理
        if (method.isAnnotationPresent(Transactional.class)) {
            SqlSession sqlSession = sqlSessionFactory.openSession(true);
            try {
                AnalyticMethod.sqlSession.set(sqlSession);
                sqlSession.beginTransaction();
                log.info("1、开启事务");
                result = proxyChain.doProxyChain();
                sqlSession.commitTransaction();
                log.info("2、提交事务");
            } catch (Exception e) {
                sqlSession.rollbackTransaction();
                log.error("3、回滚事务");
                throw e;
            }
        } else {
            SqlSession sqlSession = sqlSessionFactory.openSession();
            AnalyticMethod.sqlSession.set(sqlSession);
            result = proxyChain.doProxyChain();
        }
        return result;
    }
}