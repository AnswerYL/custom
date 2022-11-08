/**
 * @projectName custom-spring
 * @package org.answer.wx.session
 * @className org.answer.wx.session.SqlSessionDefultUtil
 */
package org.answer.wx.util;

import org.answer.wx.annotation.Table;
import org.answer.wx.base.BaseMapper;
import org.answer.wx.proxy.AnalyticMethod;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * SqlSessionDefultUtil
 * @description 默认prepareStatement
 * @author answer_wx
 * @date 2022/11/4 17:59
 * @version 1.0
 */
public class SqlSessionDefultUtil {

    static Logger log = Logger.getLogger(SqlSessionDefultUtil.class);

    public static <T> List<T> getAll(Connection connection, Class<T> clazz) {
        String tableName = MySQLUtil.getTableName(clazz);
        String sql = "select * from " + tableName;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            return MySQLUtil.getListByResult(rs, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MySQLUtil.closeAll(null, ps, rs);
        }
        return null;
    }

    public static <T> T getById(Connection connection, Class<T> clazz, Serializable id) {
        String tableName = MySQLUtil.getTableName(clazz);
        String idColumnName = MySQLUtil.getIdColumnName(clazz);
        String sql = "select * from " + tableName + " where " + idColumnName + "=?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setObject(1, id);
            rs = ps.executeQuery();
            List<T> list = MySQLUtil.getListByResult(rs, clazz);
            return list.isEmpty() ? null : list.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MySQLUtil.closeAll(null, ps, rs);
        }
        return null;
    }

    public static boolean delete(Connection connection, Class<?> clazz, Serializable id) {
        String tableName = MySQLUtil.getTableName(clazz);
        String idColumnName = MySQLUtil.getIdColumnName(clazz);
        String sql = "delete from " + tableName + " where " + idColumnName + "=?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setObject(1, id);
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MySQLUtil.closeAll(null, ps, rs);
        }
        return false;
    }

    public static boolean saveOrUpdate(Connection connection, Object object) {
        Class<?> clazz = object.getClass();
        String tableName = MySQLUtil.getTableName(clazz);
        if (tableName == null) {
            return false;
        }
        Map<String, Object> map = MySQLUtil.getMapByObject(object);
        String idColumnName = MySQLUtil.getIdColumnName(clazz);
        if (map.containsKey(idColumnName)) {
            return update(connection, tableName, map, idColumnName);
        } else {
            return save(connection, tableName, map);
        }
    }

    public static boolean save(Connection connection, Object object) {
        Class<?> clazz = object.getClass();
        if (!clazz.isAnnotationPresent(Table.class)) {
            log.error("此对象不是实体类：" + clazz.getSimpleName());
            return false;
        }
        Map<String, Object> map = MySQLUtil.getMapByObject(object);
        String tableName = MySQLUtil.getTableName(clazz);
        return save(connection, tableName, map);
    }

    public static boolean update(Connection connection, Object object) {
        Class<?> clazz = object.getClass();
        if (!clazz.isAnnotationPresent(Table.class)) {
            log.error("此对象不是实体类：" + clazz.getSimpleName());
            return false;
        }
        Map<String, Object> map = MySQLUtil.getMapByObject(object);
        String idColumnName = MySQLUtil.getIdColumnName(clazz);
        String tableName = MySQLUtil.getTableName(clazz);
        return update(connection, tableName, map, idColumnName);
    }


    private static boolean save(Connection connection, String tableName, Map<String, Object> map) {
        StringBuilder sql1 = new StringBuilder("insert into " + tableName + "(");
        StringBuilder sql2 = new StringBuilder("values(");
        for (String key : map.keySet()) {
            sql1.append(key).append(",");
            sql2.append("?,");
        }
        sql1.delete(sql1.length() - 1, sql1.length());
        sql1.append(") ");
        sql2.delete(sql2.length() - 1, sql2.length());
        sql2.append(")");
        String sql = sql1.toString() + sql2;
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql);
            int i = 1;
            for (String key : map.keySet()) {
                ps.setObject(i++, map.get(key));
            }
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MySQLUtil.closeAll(null, ps, null);
        }
        return false;
    }

    private static boolean update(Connection connection, String tableName, Map<String, Object> map, String IdColumnName) {
        Object idValue = map.get(IdColumnName);
        map.remove(IdColumnName);
        StringBuilder sql = new StringBuilder("update " + tableName + " set ");
        for (String key : map.keySet()) {
            sql.append(key).append("=?,");
        }
        sql.delete(sql.length() - 1, sql.length());
        sql.append(" where ").append(IdColumnName).append("=?");
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql.toString());
            int i = 1;
            for (String key : map.keySet()) {
                if (map.get(key) != null) {
                    ps.setObject(i++, map.get(key));
                }
            }
            ps.setObject(map.size() + 1, idValue);
            return ps.executeUpdate() == 1;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MySQLUtil.closeAll(null, ps, null);
        }
        return false;
    }

    public static <T> List<T> getAllByPage(Connection connection, Class<T> clazz, int start, int end) {
        String tableName = MySQLUtil.getTableName(clazz);
        String sql = "select * from " + tableName + " limit ?,?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            ps.setInt(1, start);
            ps.setInt(2, end);
            rs = ps.executeQuery();
            return MySQLUtil.getListByResult(rs, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MySQLUtil.closeAll(null, ps, rs);
        }
        return null;
    }

    public static int getCount(Connection connection,Class<?> clazz) {
        String tableName = MySQLUtil.getTableName(clazz);
        String sql = "select count(1) from " + tableName;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            return MySQLUtil.getCount(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MySQLUtil.closeAll(null, ps, rs);
        }
        return 0;
    }
}