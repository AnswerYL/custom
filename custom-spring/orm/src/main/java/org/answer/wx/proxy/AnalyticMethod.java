/**
 * @projectName custom-spring
 * @package org.answer.wx.proxy
 * @className org.answer.wx.proxy.AnalyticMethod
 */
package org.answer.wx.proxy;

import org.answer.wx.annotation.*;
import org.answer.wx.base.BaseMapper;
import org.answer.wx.session.SqlSession;
import org.answer.wx.util.MySQLUtil;
import org.answer.wx.util.SqlSessionDefultUtil;
import org.answer.wx.util.TypeUtil;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.lang.reflect.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AnalyticMethod
 * @description
 * @author answer_wx
 * @date 2022/11/4 17:17
 * @version 1.0
 */
public class AnalyticMethod {

    static Logger log = Logger.getLogger(AnalyticMethod.class);

    // 事务conn位置
    public static final ThreadLocal<SqlSession> sqlSession = new ThreadLocal<>();

    public static Object invoke(Class clazz, Object targetObject, Method targetMethod, Object[] methodParams) {
        if (objectMethod(targetMethod)) {
            return targetObject.getClass().getName();
        }
        SqlSession session = sqlSession.get();
        Connection connection = null;
        if (session == null) {
            throw new RuntimeException("connection not exist");
        } else {
            connection = session.getConnection();
        }
        if (baseSql(targetMethod)) {
            return baseExec(clazz, targetObject, targetMethod, connection, methodParams);
        }
        Object result = null;
        StringBuilder sql = new StringBuilder();
        if (targetMethod.isAnnotationPresent(Select.class)) {
            result = execSelect(targetMethod, methodParams, sql, connection);
        } else if (targetMethod.isAnnotationPresent(Update.class)
                || targetMethod.isAnnotationPresent(Insert.class)
                || targetMethod.isAnnotationPresent(Delete.class)) {
            result = execUpdate(targetMethod, methodParams, sql, connection);
        }
        Class<?> returnType = targetMethod.getReturnType();
        if (returnType == Void.TYPE) {
            return null;
        } else if (TypeUtil.isComposite(returnType)) {
            return result;
        } else if (TypeUtil.isString(returnType)) {
            return result == null ? null : result.toString();
        } else {
            return TypeUtil.typeConversion(returnType, result);
        }
    }


    private static boolean objectMethod(Method targetMethod) {
        Method[] methods = Object.class.getDeclaredMethods();
        for (Method method : methods) {
            if (method.equals(targetMethod)) {
                return true;
            }
        }
        return false;
    }

    private static boolean baseSql(Method targetMethod) {
        Method[] methods = BaseMapper.class.getMethods();
        for (Method method : methods) {
            if (targetMethod.equals(method)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取接口的类型索引
     *
     * @param clazz
     * @param index
     * @return
     */
    public static Class<?> getInterfaceT(Class clazz, int index) {
        Type[] types = clazz.getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[index];
        Type type = parameterizedType.getActualTypeArguments()[index];
        return checkType(type, index);
    }

    /**
     * 获取类上的泛型T
     *
     * @param o     接口
     * @param index 泛型索引
     */
    public static Class<?> getClassT(Object o, int index) {
        Type type = o.getClass().getGenericSuperclass();
        return checkType(type, index);
    }


    /**
     * @param type
     * @param index
     * @return
     */
    private static Class<?> checkType(Type type, int index) {
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        } else if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type t = parameterizedType.getActualTypeArguments()[index];
            return checkType(t, index);
        } else {
            String className = type == null ? "null" : type.getClass().getName();
            throw new IllegalArgumentException("Expected a Class, ParameterizedType"
                    + ", but <" + type + "> is of type " + className);
        }
    }

    private static Object baseExec(Class clazz, Object targetObject, Method targetMethod, Connection connection, Object[] methodParams) {
        String name = targetMethod.getName();
        log.info("基础 sql 方法" + name);
        if ("saveOrUpdate".equals(name)) {
            return SqlSessionDefultUtil.saveOrUpdate(connection, methodParams[0]);
        }
        if ("save".equals(name)) {
            return SqlSessionDefultUtil.save(connection, methodParams[0]);
        }
        if ("update".equals(name)) {
            return SqlSessionDefultUtil.update(connection, methodParams[0]);
        }
        Class<?> interfaceT = getInterfaceT(clazz, 0);
        if ("getAll".equals(name)) {
            return SqlSessionDefultUtil.getAll(connection, interfaceT);
        }
        if ("getById".equals(name)) {
            return SqlSessionDefultUtil.getById(connection, interfaceT, (Serializable) methodParams[0]);
        }
        if ("delete".equals(name)) {
            return SqlSessionDefultUtil.delete(connection, interfaceT, (Serializable) methodParams[0]);
        }
        if ("getAllByPage".equals(name)) {
            return SqlSessionDefultUtil.getAllByPage(connection, interfaceT, Integer.valueOf(methodParams[0].toString()), Integer.valueOf(methodParams[1].toString()));
        }
        if ("getCount".equals(name)) {
            return SqlSessionDefultUtil.getCount(connection, interfaceT);
        }

        if ("close".equals(name)) {
            MySQLUtil.closeAll(connection, null, null);
            return null;
        }

        if ("flush".equals(name)) {
            MySQLUtil.flush(connection);
            return null;
        }
        return null;
    }

    private static Object execSelect(Method targetMethod, Object[] methodParams, StringBuilder sql, Connection connection) {
        Select select = targetMethod.getAnnotation(Select.class);
        sql.append(select.value());
        Parameter[] parameters = targetMethod.getParameters();
        initParameter(parameters, sql, methodParams);
        log.info("执行查询语句：" + sql);
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = connection.prepareStatement(sql.toString());
            rs = ps.executeQuery();
            Type returnType = targetMethod.getGenericReturnType();
            Class<?> clazz = targetMethod.getReturnType();
            if (clazz.isInterface()) {
                if (returnType instanceof ParameterizedType) {
                    Type[] types = ((ParameterizedType) returnType).getActualTypeArguments();
                    Type t = types[0];
                    return MySQLUtil.getListByResult(rs, Class.forName(t.getTypeName()));
                }
            } else {
                List<?> list = MySQLUtil.getListByResult(rs, clazz);
                if (list.size() == 0) {
                    return null;
                } else if (list.size() == 1) {
                    return list.get(0);
                } else {
                    return list;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MySQLUtil.closeAll(connection, ps, rs);
        }
        return null;
    }

    private static Object execUpdate(Method targetMethod, Object[] methodParams, StringBuilder sql, Connection connection) {
        String value;
        if (targetMethod.isAnnotationPresent(Insert.class)) {
            Insert annotation = targetMethod.getAnnotation(Insert.class);
            value = annotation.value();
        } else if (targetMethod.isAnnotationPresent(Update.class)) {
            Update annotation = targetMethod.getAnnotation(Update.class);
            value = annotation.value();
        } else {
            Delete annotation = targetMethod.getAnnotation(Delete.class);
            value = annotation.value();
        }
        sql.append(value);
        initParameter(targetMethod.getParameters(), sql, methodParams);
        log.info("执行 sql 语句：" + sql.toString());
        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement(sql.toString());
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MySQLUtil.closeAll(connection, ps, null);
        }
        return null;
    }

    private static void initParameter(Parameter[] parameters, StringBuilder sql, Object[] methodParams) {
        if (methodParams == null) {
            return;
        }
        for (int i = 0; i < methodParams.length; i++) {
            if (parameters[i].isAnnotationPresent(Param.class)) {
                Param param = parameters[i].getAnnotation(Param.class);
                String value = param.value();
                String tmp = "";
                if (TypeUtil.isComposite(parameters[i].getType())) {
                    Object o = methodParams[i];
                    Map<String, Object> map = new HashMap<>();
                    Field[] fields = o.getClass().getDeclaredFields();
                    try {
                        for (Field field : fields) {
                            field.setAccessible(true);
                            if (field.get(o) != null) {
                                map.put(field.getName(), field.get(o));
                            } else {
                                if (TypeUtil.isNumber(field.getType())) {
                                    map.put(field.getName(), 0);
                                } else {
                                    map.put(field.getName(), "");
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (String key : map.keySet()) {
                        if (map.get(key) == null) {
                            continue;
                        }
                        if (TypeUtil.isNumber(map.get(key).getClass())) {
                            tmp = sql.toString().replace("#{" + value + "." + key + "}", map.get(key).toString());
                        } else {
                            tmp = sql.toString().replace("#{" + value + "." + key + "}", "'" + map.get(key) + "'");
                        }
                        sql = sql.delete(0, sql.length()).append(tmp);
                    }
                } else if (TypeUtil.isNumber(parameters[i].getType())) {
                    tmp = sql.toString().replace("#{" + value + "}", methodParams[i].toString());
                } else {
                    tmp = sql.toString().replace("#{" + value + "}", "'" + methodParams[i].toString() + "'");
                }
                sql.delete(0, sql.length()).append(tmp);
            }
        }
    }
}