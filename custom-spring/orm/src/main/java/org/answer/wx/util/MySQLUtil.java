/**
 * @projectName custom-spring
 * @package org.answer.wx.util
 * @className org.answer.wx.util.MySQLUtil
 */
package org.answer.wx.util;

import org.answer.wx.annotation.Column;
import org.answer.wx.annotation.Id;
import org.answer.wx.annotation.NoColumn;
import org.answer.wx.annotation.Table;
import org.answer.wx.session.SqlConfig;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.*;

/**
 * MySQLUtil
 * @description
 * @author answer_wx
 * @date 2022/11/4 18:01
 * @version 1.0
 */
public class MySQLUtil {

    static Logger log = Logger.getLogger(MySQLUtil.class);

    private static SqlConfig config;
    private static Connection connection;

    public static void setConfig(SqlConfig config) {
        MySQLUtil.config = config;
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(config.url, config.username, config.password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static boolean tableIsExist(String tableName) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            String sql = "show tables";
            ps = connection.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getObject(1).toString().equals(tableName)) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(connection, ps, rs);
        }
        return false;
    }

    public static Map<String, String[]> getTableStructure(String tableName) {
        Map<String, String[]> map = new HashMap<>();
        String sql = "select column_name,data_type,CHARACTER_MAXIMUM_LENGTH from information_schema.columns where table_name=?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sql);
            ps.setString(1, tableName);
            rs = ps.executeQuery();
            while (rs.next()) {
                String[] strs = new String[2];
                strs[0] = rs.getString(2);
                strs[1] = rs.getString(3);
                if (strs[1] == null) {
                    strs[1] = "-1";
                }
                map.put(rs.getString(1), strs);
            }
            return map;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeAll(connection, ps, rs);
        }
        return map;
    }

    public static void closeAll(Connection connection, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        System.gc();
    }

    /**
     * 字段名、类型、长度、是否索引、是否唯一约束、是否不为空、是否ID、是否自增
     * @param sql
     * @param name
     * @param type
     * @param len
     * @param isUnique
     * @param isNullable
     * @param isId
     * @param isIncrement
     * @return
     */
    public static String generateSQL(StringBuilder sql, String name, String type, int len, boolean isUnique, boolean isNullable, boolean isId, boolean isIncrement) {
        return sql.append(name + " " + type + ((len == -1) ? "" : "(" + len + ")") + " " +
                (isId ? "primary key " + ((isIncrement) ? "auto_increment" : "") : "") + (isUnique ? " unique" : "") +
                (isNullable ? " notnull" : "")).toString();
    }

    public static List<String> getIndexNameList(Field[] fields) {
        List<String> list = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                if (column.index()) {
                    if (column.value().equals("")) {
                        list.add(field.getName());
                    } else {
                        list.add(column.value());
                    }
                }
            }
        }
        return list;
    }

    /**
     * 执行sql语句
     * @param sql
     * @return
     */
    public static boolean exeSql(String sql) {
        PreparedStatement ps = null;
        try {
            connection = getConnection();
            ps = connection.prepareStatement(sql);
            return ps.execute();
        } catch (Exception e) {
            if (e.getClass() != SQLSyntaxErrorException.class) {
                e.printStackTrace();
            }
        } finally {
            closeAll(connection, ps, null);
        }
        return false;
    }

    /**
     * 根据类型获取表明
     * @param clazz
     * @return
     */
    public static String getTableName(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Table.class)) {
            Table table = clazz.getAnnotation(Table.class);
            return table.name();
        } else {
            log.error("该对象不是实体：" + clazz.getSimpleName());
        }
        return null;
    }

    /**
     * 根据结果返回list
     * @param rs
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> getListByResult(ResultSet rs, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        try {
            while (rs.next()) {
                T instance = clazz.getConstructor().newInstance();
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (!field.isAnnotationPresent(NoColumn.class)) {
                        Column column = field.getAnnotation(Column.class);
                        String name;
                        if (column == null || "".equals(column.value())) {
                            name = field.getName();
                        } else {
                            name = column.value().replaceAll("`", "");
                        }
                        field.setAccessible(true);
                        if (rs.getObject(name) == null) {
                            continue;
                        }
                        field.set(instance, TypeUtil.typeConversion(field.getType(), rs.getObject(name)));
                    }
                }
                list.add(instance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取主键列名
     * @param clazz
     * @return
     */
    public static String getIdColumnName(Class<?> clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class) && field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                if ("".equals(column.value())) {
                    return field.getName();
                } else {
                    return column.value();
                }
            } else if (field.isAnnotationPresent(Id.class)) {
                return field.getName();
            }
        }
        return null;
    }

    public static Map<String, Object> getMapByObject(Object object) {
        Map<String, Object> map = new LinkedHashMap<>();
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                if (!field.isAnnotationPresent(NoColumn.class)) {
                    field.setAccessible(true);
                    Object value = field.get(object);
                    if (value == null) {
                        continue;
                    }
                    log.info("封装map集合：key=" + field.getName() + ",value=" + value);
                    if (field.isAnnotationPresent(Column.class)) {
                        Column column = field.getAnnotation(Column.class);
                        if ("".equals(column.value())) {
                            map.put(field.getName(), value);
                        } else {
                            map.put(column.value(), value);
                        }
                    } else {
                        map.put(field.getName(), value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static int getCount(ResultSet rs) {
        try {
            if (rs != null) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void flush(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}