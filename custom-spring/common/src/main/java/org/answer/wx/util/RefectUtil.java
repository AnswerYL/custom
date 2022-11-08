/**
 * @projectName custom-spring
 * @package org.answer.wx.util
 * @className org.answer.wx.util.RefectUtil
 */
package org.answer.wx.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * RefectUtil
 * @description 反射工具类
 * @author answer_wx
 * @date 2022/11/3 11:37
 * @version 1.0
 */
public class RefectUtil {
    public static <T> T newInstance(String clsName) {

        if (clsName == null) {
            return null;
        }
        Class<?> cls = null;
        try {
            cls = Class.forName(clsName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        if (cls == null) {
            return null;
        }
        return newInstance(cls);
    }

    public static <T> T newInstance(Class<?> cls) {

        if (cls == null) {
            return null;
        }

        Constructor con = null;
        T result = null;
        try {
            con = cls.getConstructor();
            result = (T) con.newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException ex) {
            ex.printStackTrace();
        }

        return result;
    }
}