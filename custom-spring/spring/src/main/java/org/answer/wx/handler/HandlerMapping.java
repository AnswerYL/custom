/**
 * @projectName custom-spring
 * @package org.answer.wx.handler
 * @className org.answer.wx.handler.HandlerMapping
 */
package org.answer.wx.handler;

import org.answer.wx.util.FilePath;
import org.answer.wx.util.JsonUtil;
import org.answer.wx.web.annotation.RequestMapping;
import org.answer.wx.web.annotation.RequestParam;
import org.answer.wx.web.annotation.ResponseBody;
import org.answer.wx.web.enums.RequestMethod;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * HandlerMapping
 * @description 处理器
 * @author answer_wx
 * @date 2022/11/8 14:41
 * @version 1.0
 */
public class HandlerMapping {
    Logger log = Logger.getLogger(HandlerMapping.class);

    private static Map<String, HandlerMethod> handlerMethodMap = new ConcurrentHashMap<>();

    public static void registerMapping(Class<?> clazz, Object bean) {
        String url = "";
        if (clazz.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping req = clazz.getAnnotation(RequestMapping.class);
            url = preUrl(req.value());
        }
        Method[] methods = clazz.getDeclaredMethods();
        HandlerMethod handlerMethod;
        for (Method method : methods) {
            if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
                String childUrl = preUrl(requestMapping.value());
                handlerMethod = new HandlerMethod(bean, method);
                handlerMethodMap.put(url + childUrl, handlerMethod);
            }
        }

    }

    private static String preUrl(String value) {
        if (value == null || value.length() <= 0) {
            return value;
        }
        if (value.charAt(0) != '/') {
            value = '/' + value;
        }
        if (value.endsWith("/")) {
            value = value.substring(0, value.length() - 1);
        }
        return value;
    }

    public void request(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String url = req.getRequestURI();
        if (!handlerMethodMap.containsKey(url)) {
            resp.setContentType("text/html; charset=utf-8");
            resp.getWriter().println("<h1>" + url + "请求不存在，404</h1>");
            return;
        }
        HandlerMethod handlerMethod = handlerMethodMap.get(url);
        if (!validMethodType(req, resp, handlerMethod.method)) {
            return;
        }
        // 入参
        Object[] parameter = initParameter(req, resp, handlerMethod.method);

        Object result = null;

        try {
            result = handlerMethod.method.invoke(handlerMethod.bean, parameter);
        } catch (InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            resp.setContentType("text/html; charset=utf-8");
            resp.getWriter().println("<h1>" + url + "请求出错" + e.getMessage() + "</h1>");
            return;
        }
        if (handlerMethod.method.isAnnotationPresent(ResponseBody.class)) {
            // 返回json请求
            resp.setContentType("text/json; charset=utf-8");
            resp.getWriter().println(JsonUtil.ObjToJsonStr(result));
            return;
        }

        if (result instanceof String) {
            // 是否重定向
            if (result.toString().startsWith("redirect:")) {
                url = result.toString();
                log.info("重定向URL：" + url);
                // 截取redirect: 之后的地址
                url = "/" + url.substring(9);
                if (handlerMethodMap.containsKey(url)) {
                    resp.sendRedirect(".." + url);
                    return;
                } else {
                    resp.getWriter().println("<h1>" + url + " not find，404</h1>");
                    return;
                }
            }
            String uuidKey = UUID.randomUUID().toString();
            String uuidValue = UUID.randomUUID().toString();
            req.setAttribute(uuidKey, uuidValue);
            PrintWriter pw = new PrintWriter(new File(FilePath.RESOURCES_PATH + "key.properties"));
            pw.write(uuidKey + "=" + uuidValue);
            req.getRequestDispatcher(result.toString() + ".red").forward(req, resp);
        }
        if (result instanceof ModelAndView) {
            ModelAndView modelAndView = (ModelAndView) result;
            for (String key : modelAndView.keySet()) {
                req.setAttribute(key, modelAndView.get(key));
            }
            String uuidKey = UUID.randomUUID().toString();
            String uuidValue = UUID.randomUUID().toString();
            PrintWriter pw = new PrintWriter(new File(FilePath.RESOURCES_PATH + "key.properties"));
            pw.write(uuidKey + "=" + uuidValue);
            Cookie cookie = new Cookie(uuidKey, uuidValue);
            resp.addCookie(cookie);
            req.getRequestDispatcher(modelAndView.getViewName() + ".red").forward(req, resp);
        } else {
            resp.getWriter().println("<h1>返回值异常，500</h1>");
        }
    }

    private boolean validMethodType(HttpServletRequest req, HttpServletResponse resp, Method method) throws IOException {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        RequestMethod[] methods = requestMapping.method();
        if (methods.length == 0) {
            return true;
        }
        String methodType = req.getMethod();
        for (RequestMethod requestMethod : methods) {
            if (requestMethod.toString().equals(methodType)) {
                return true;
            }
        }
        resp.setContentType("text/html; charset=utf-8");
        resp.getWriter().println("<h1>你的请求类型为：" + methodType + "，但是接受类型是：" + Arrays.toString(requestMapping.method()) + "，请求错误，405</h1>");
        return false;
    }

    private Object[] initParameter(HttpServletRequest req, HttpServletResponse resp, Method method) {
        Parameter[] parameters = method.getParameters();
        Map<String, String[]> parameMap = req.getParameterMap();

        Object[] args = new Object[parameters.length];
        // 参数解析
        for (int i = 0; i < args.length; i++) {
            if (parameters[i].getType() == HttpServletRequest.class) {
                args[i] = req;
            } else if (parameters[i].getType() == HttpServletResponse.class) {
                args[i] = resp;
            } else if (parameters[i].getType() == ModelAndView.class) {
                args[i] = new ModelAndView();
            } else if (parameters[i].isAnnotationPresent(RequestParam.class)) {
                RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
                String key = requestParam.value();
                if (parameMap.containsKey(key)) {
                    if (parameMap.get(key).length == 1) {
                        args[i] = setArgs(parameters[i], parameMap.get(key)[0]);
                    } else {
                        args[i] = parameMap.get(key);
                    }
                }
            } else {
                try {
                    Object obj = parameters[i].getType().getConstructor().newInstance();
                    Field[] fields = parameters[i].getType().getDeclaredFields();
                    for (Field field : fields) {
                        if (parameMap.containsKey(field.getName())) {
                            field.setAccessible(true);
                            if (parameMap.get(field.getName()) != null) {
                                setValue(field, obj, parameMap.get(field.getName())[0]);
                            }
                        }
                    }
                    args[i] = obj;
                } catch (Exception e) {
                    log.error("无法创建对象：" + parameters[i].getType());
                    e.printStackTrace();
                }
                log.error("无法初始化的类型：" + parameters[i].getType());
            }
        }
        return args;
    }

    private Object setArgs(Parameter parameter, String s) {
        if (parameter.getType() == Long.class || parameter.getType() == long.class) {
            return Long.parseLong(s);
        }
        if (parameter.getType() == Integer.class || parameter.getType() == int.class) {
            return Integer.parseInt(s);
        }
        if (parameter.getType() == Short.class || parameter.getType() == short.class) {
            return Short.parseShort(s);
        }
        if (parameter.getType() == Byte.class || parameter.getType() == byte.class) {
            return Byte.parseByte(s);
        }
        if (parameter.getType() == Float.class || parameter.getType() == float.class) {
            return Float.parseFloat(s);
        }
        if (parameter.getType() == Double.class || parameter.getType() == double.class) {
            return Double.parseDouble(s);
        }
        if (parameter.getType() == Boolean.class || parameter.getType() == boolean.class) {
            return Boolean.parseBoolean(s);
        }
        return s;
    }

    private void setValue(Field field, Object obj, String s) throws Exception {
        if (field.getType() == String.class) {
            field.set(obj, s);
        } else if (field.getType() == Integer.class || field.getType() == int.class) {
            field.set(obj, Integer.parseInt(s));
        } else if (field.getType() == Double.class || field.getType() == double.class) {
            field.set(obj, Double.parseDouble(s));
        } else if (field.getType() == Float.class || field.getType() == float.class) {
            field.set(obj, Float.parseFloat(s));
        } else if (field.getType() == Long.class || field.getType() == long.class) {
            field.set(obj, Long.parseLong(s));
        } else if (field.getType() == Byte.class || field.getType() == byte.class) {
            field.set(obj, Byte.parseByte(s));
        } else if (field.getType() == Short.class || field.getType() == short.class) {
            field.set(obj, Short.parseShort(s));
        } else if (field.getType() == Character.class || field.getType() == char.class) {
            field.set(obj, s);
        }
    }
}