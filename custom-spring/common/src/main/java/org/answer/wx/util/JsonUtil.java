/**
 * @projectName custom-spring
 * @package org.answer.wx.util
 * @className org.answer.wx.util.JsonUtil
 */
package org.answer.wx.util;

import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.*;

/**
 * JsonUtil
 * @description JSON解析
 * @author answer_wx
 * @date 2022/11/3 11:06
 * @version 1.0
 */
public class JsonUtil {
    public static Logger logger = Logger.getLogger(JsonUtil.class);

    private static String subJson(String json) {
        return json.substring(1, json.length() - 1);
    }

    public static <T> T strToObj(String json, Class<T> cls) {
        if (json == null || json.length() < 2) {
            logger.error("无效的json字符");
            return null;
        }
        try {
            T t = cls.getDeclaredConstructor().newInstance();
            if (t instanceof Map || t instanceof Collection) {
                if (json.charAt(0) == '[' && json.charAt(json.length() - 1) == ']') {
                    return toTypes(json, t);
                } else {
                    logger.error("此json字符无法转换为集合类型");
                    return null;
                }
            } else {
                if (json.charAt(0) == '{' && json.charAt(json.length() - 1) == '}') {
                    return toType(json, t);
                } else {
                    logger.error("此json字符无法转换为复合类型");
                    return null;
                }
            }
        } catch (Exception e) {
            logger.error(cls.getTypeName() + "类型的对象创建失败");
            e.printStackTrace();
        }
        return null;
    }

    private static <T> T toType(String json, T t) throws Exception {
        json = subJson(json);
        if (t.getClass() == String.class || t.getClass() == Character.class) {
            return (T) json;
        } else {
            String[] split = json.split(",");
            HashMap<String, String> map = new HashMap<>();
            for (String s : split) {
                String[] kvs = s.split(":");
                map.put(kvs[0].substring(1, kvs[0].length() - 1), kvs[1]);
            }
            for (String s : map.keySet()) {
                Field field = t.getClass().getField(s);
                field.setAccessible(true);
                Class<?> type = field.getType();
                if (type == String.class || type == Character.class || type == char.class) {
                    field.set(t, map.get(s).substring(1, map.get(s).length() - 1));
                } else if (type == int.class || type == Integer.class) {
                    field.set(t, Integer.parseInt(map.get(s)));
                } else if (type == byte.class || type == Byte.class) {
                    field.set(t, Byte.parseByte(map.get(s)));
                }
            }
        }
        return t;
    }

    private static <T> T toTypes(String json, T t) {
        json = subJson(json);
        return null;
    }

    /**
     * 把一个对象转换为Json字符串
     *
     * @param object
     * @return
     */
    public static String ObjToJsonStr(Object object)
    {
        if(object == null)
        {
            return "";
        }
        if(object instanceof String || object.getClass().isPrimitive()) {
            return String.valueOf(object);
        }
        String result = typeOrField(object);
        if (isList(object) || isSet(object))
        {
            if (result.toCharArray()[0] == '[')
            {
                return typeOrField(object);
            } else
            {
                String temp=typeOrField(object);
                if(temp.length()>1)
                {
                    return "[" + typeOrField(object) + "]";
                }else
                {
                    return "[" + typeOrField(object);
                }
            }
        } else
        {
            if (result.toCharArray()[0] == '{')
            {
                return typeOrField(object);
            } else
            {
                return "{" + typeOrField(object) + "}";
            }
        }
    }

    private static String typeOrField(Object object)
    {
        if (object instanceof Number || object.getClass() == Boolean.class)
        {
            return object.toString();
        } else if (object.getClass() == String.class || object.getClass() == Character.class)
        {
            return "\"" + object + "\"";
        } else if (isList(object))
        {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            List list = (List) object;
            int size = list.size();
            for (Object o : list) {
                sb.append(typeOrField(o)).append(",");
            }
            sb.delete(sb.length() - 1, sb.length());
            sb.append("]");
            return sb.toString();
        } else if (isSet(object))
        {
            StringBuilder sb = new StringBuilder();
            Set set = (Set) object;
            for (Object o : set)
            {
                sb.append(typeOrField(o)).append(",");
            }
            sb.delete(sb.length() - 1, sb.length());
            return sb.toString();
        } else if (isMap(object))
        {
            Map map = (Map) object;
            StringBuilder sb = new StringBuilder();
            for (Object o : map.keySet())
            {
                sb.append("\"").append(o).append("\"").append(":").append(typeOrField(map.get(o))).append(",");
            }
            sb.delete(sb.length() - 1, sb.length());
            return sb.toString();
        } else
        {
            Class clazz = object.getClass();
            Field[] fields = clazz.getDeclaredFields();
            StringBuilder sb = new StringBuilder();
            for (Field f : fields)
            {
                sb.append(fieldToStr(f, object));
            }
            sb.delete(sb.length() - 1, sb.length());
            return "{" + sb.toString() + "}";
        }
    }

    private static String fieldToStr(Field field, Object object)
    {
        StringBuilder sb = new StringBuilder();
        try
        {
            field.setAccessible(true);
            if(field.get(object)==null)
            {
                // 如果字段为空，则返回空字符串
                return "";
            }
            if (field.getType() == String.class || field.getType() == char.class || field.getType() == Character.class)
            {
                sb.append("\"").append(field.getName()).append("\":\"").append(field.get(object)).append("\",");
            } else if (field.get(object) instanceof Number)
            {
                sb.append("\"").append(field.getName()).append("\":").append(field.get(object)).append(",");
            } else if (isList(field.get(object)))
            {
                sb.append("\"").append(field.getName()).append("\":").append(typeOrField(field.get(object))).append(",");
            } else if (isMap(object))
            {
                sb.append("\"").append(field.getName()).append("\":").append(typeOrField(field.get(object))).append(",");
            } else if (isSet(object))
            {
                sb.append("\"").append(field.getName()).append("\":").append(typeOrField(field.get(object))).append(",");
            } else
            {
                sb.append("{").append(typeOrField(object)).append("}");
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private static boolean isList(Object object)
    {
        return object instanceof List;
    }

    private static boolean isMap(Object object)
    {
        return object instanceof Map;
    }

    private static boolean isSet(Object object)
    {
        return object instanceof Set;
    }
}