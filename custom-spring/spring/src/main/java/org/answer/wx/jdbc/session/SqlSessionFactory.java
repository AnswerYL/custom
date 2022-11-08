/**
 * @projectName custom-spring
 * @package org.answer.wx.jdbc.session
 * @className org.answer.wx.jdbc.session.SqlSessionFactory
 */
package org.answer.wx.jdbc.session;

import org.answer.wx.session.SqlConfig;
import org.answer.wx.session.SqlSession;

/**
 * SqlSessionFactory
 * @description sql session 工厂接口
 * @author answer_wx
 * @date 2022/11/4 16:50
 * @version 1.0
 */
public interface SqlSessionFactory {
    SqlSession openSession();

    SqlSession openSession(boolean transaction);

    SqlConfig getConfiguration();
}