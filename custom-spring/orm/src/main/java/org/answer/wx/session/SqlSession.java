/**
 * @projectName custom-spring
 * @package org.answer.wx.session
 * @className org.answer.wx.session.SqlSession
 */
package org.answer.wx.session;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * SqlSession
 * @description
 * @author answer_wx
 * @date 2022/11/4 17:42
 * @version 1.0
 */
public class SqlSession {

    Logger log = Logger.getLogger(SqlSession.class);

    private Connection connection;
    private boolean transaction;

    public Connection getConnection() {
        return connection;
    }

    public boolean isTransaction() {
        return transaction;
    }

    public SqlSession(Connection connection) {
        this.connection = connection;
        this.transaction = false;
    }

    public SqlSession(Connection connection, boolean transaction) {
        this.connection = connection;
        this.transaction = transaction;
    }

    public void beginTransaction() {
        if (connection != null) {
            try {
                connection.setAutoCommit(false);
            } catch (SQLException e) {
                log.error("开启事务失败！");
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    public void commitTransaction() {
        if (connection != null) {
            try {
                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
                log.error("事务提交失败！");
                throw new RuntimeException(e);
            } finally {
                // 释放链接资源
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void rollbackTransaction() {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
                log.error("事务回滚失败！");
                throw new RuntimeException(e);
            } finally {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}