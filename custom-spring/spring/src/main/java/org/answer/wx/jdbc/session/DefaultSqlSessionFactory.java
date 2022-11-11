/**
 * @projectName custom-spring
 * @package org.answer.wx.session
 * @className org.answer.wx.session.DefaultSqlSessionFactory
 */
package org.answer.wx.jdbc.session;

import org.answer.wx.session.SqlConfig;
import org.answer.wx.session.SqlSession;
import org.answer.wx.util.MySQLUtil;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DefaultSqlSessionFactory
 * @description 默认sqlsession实现类
 * @author answer_wx
 * @date 2022/11/4 17:51
 * @version 1.0
 */
public class DefaultSqlSessionFactory implements SqlSessionFactory {

    Logger log = Logger.getLogger(DefaultSqlSessionFactory.class);

    private SqlConfig config;

    // 非事务链接
    private Connection connection;

    public DefaultSqlSessionFactory(SqlConfig config) {
        this.config = config;
    }

    @Override
    public SqlSession openSession() {
        return new SqlSession(getConnection());
    }

    @Override
    public SqlSession openSession(boolean transaction) {
        return transaction ? new SqlSession(getConnection(), true) : openSession();
    }

    @Override
    public SqlConfig getConfiguration() {
        return null;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(config.url, config.username, config.password);
                log.info("数据库链接成功！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public Connection newConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(config.url, config.username, config.password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}