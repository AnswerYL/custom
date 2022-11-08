/**
 * @projectName custom-spring
 * @package org.answer.wx.aop.context
 * @className org.answer.wx.aop.context.ActionApplicationContext
 */
package org.answer.wx.context;

import org.answer.wx.aop.annotation.Aspect;
import org.answer.wx.aop.aspectj.ProxyFactory;
import org.answer.wx.aop.aspectj.proxy.AspectProxy;
import org.answer.wx.aop.aspectj.proxy.Proxy;
import org.answer.wx.beans.annotation.*;
import org.answer.wx.beans.factory.AbstractActionFactory;
import org.answer.wx.beans.factory.BeanFactory;
import org.answer.wx.handler.HandlerMapping;
import org.answer.wx.transaction.TransactionProxy;
import org.answer.wx.web.annotation.Controller;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * ActionApplicationContext
 * @description action容器
 * @author answer_wx
 * @date 2022/11/3 16:29
 * @version 1.0
 */
public class ActionApplicationContext extends AbstractActionFactory {

    Logger log = Logger.getLogger(ActionApplicationContext.class);

    // 分隔符号
    public static final String SEPARATE;
    // 项目路径
    public static final String PROJECT_PATH;

    // 获取IOC容器
    private BeanFactory beanFactory;

    // 代理对象容器
    private Map<String, Object> proxyMap = new HashMap<>();

    static {
        if (System.getProperty("os.name").startsWith("Windows")) {
            SEPARATE = "\\";
        } else {
            SEPARATE = "/";
        }
        PROJECT_PATH = System.getProperty("user.dir");
    }

    public Map<String, Object> getProxyMap() {
        return proxyMap;
    }

    /**
     * 扫描并加载组件
     * @param clazz
     * @param properties
     */
    @Override
    protected void scanComponent(Class clazz, Properties properties) {
        ComponentScan componentScan = (ComponentScan) clazz.getDeclaredAnnotation(ComponentScan.class);
        loadComponent(componentScan.value(), properties);
    }

    /**
     * 加载组件
     * @param basePackage
     * @param properties
     */
    public void loadComponent(String basePackage, Properties properties) {
        try {
            String path = basePackage.replaceAll("\\.", "/");
            Enumeration<URL> resources = this.getClass().getClassLoader().getResources(path);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                if (url == null) {
                    continue;
                }
                loadComponent(basePackage, url, properties);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadComponent(String basePackage, URL url, Properties properties) {
        String protocol = url.getProtocol();
        if ("file".equalsIgnoreCase(protocol)) {
            String path = url.getPath().replaceAll("%20", " ");
            loadComponentByPath(basePackage, path, properties);
        }
        if ("jar".equalsIgnoreCase(protocol)) {
            loadCompenentByJar(url, properties);
        }
    }

    private void loadCompenentByJar(URL url, Properties prop) {
        try {
            JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
            if (jarURLConnection == null) {
                return;
            }
            JarFile jarFile = jarURLConnection.getJarFile();
            if (jarFile == null) {
                return;
            }
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                String name = jarEntry.getName();
                if (name.endsWith(".class")) {
                    String className = name.substring(0, name.lastIndexOf(".")).replaceAll("\\\\", ".").replaceAll("/", ".");
                    loadComponentByClazz(className, prop);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadComponentByPath(String basePackage, String path, Properties prop) {
        File[] files = new File(path).listFiles(file -> (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory());
        assert files != null;
        for (File file : files) {
            String fileName = file.getName();
            if (file.isDirectory()) {
                String childPath = path + SEPARATE + fileName;
                loadComponentByPath(basePackage + "." + fileName, childPath, prop);
            } else {
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                className = basePackage + "." + className;
                loadComponentByClazz(className, prop);
            }
        }
    }

    private void loadComponentByClazz(String clazzName, Properties prop) {
        try {
            Class<?> clazz = Class.forName(clazzName);
            if (clazz.isAnnotationPresent(Component.class) || clazz.isAnnotationPresent(Controller.class)
                    || clazz.isAnnotationPresent(Service.class) || clazz.isAnnotationPresent(Configuration.class)) {
                Object bean = loadComponent(clazz, prop);
                if (clazz.isAnnotationPresent(Controller.class)) {
                    HandlerMapping.registerMapping(clazz, bean);
                }
            }
            if (clazz.isAnnotationPresent(ConfigurationProperties.class)) {
                ConfigurationProperties configurationProperties = clazz.getAnnotation(ConfigurationProperties.class);
                String prefix = configurationProperties.prefix();
                Field[] fields = clazz.getDeclaredFields();
                Object bean = clazz.getConstructor().newInstance();
                beanMap.put(clazz.getName(), bean);
                for (Field field : fields) {
                    String key = prefix + "." + field.getName();
                    if (prop.containsKey(key)) {
                        field.setAccessible(true);
                        String value = prop.getProperty(key);
                        setField(bean, field, value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void setField(Object bean, Field field, String value) throws IllegalAccessException {
        if (field.getType() == String.class || field.getType() == char.class || field.getType() == Character.class) {
            field.set(bean, value);
        } else if (field.getType() == Integer.class || field.getType() == int.class) {
            field.set(bean, Integer.parseInt(value));
        } else if (field.getType() == Double.class || field.getType() == double.class) {
            field.set(bean, Double.parseDouble(value));
        } else if (field.getType() == Float.class || field.getType() == float.class) {
            field.set(bean, Float.parseFloat(value));
        } else if (field.getType() == Long.class || field.getType() == long.class) {
            field.set(bean, Long.parseLong(value));
        } else if (field.getType() == Short.class || field.getType() == short.class) {
            field.set(bean, Short.parseShort(value));
        } else if (field.getType() == Byte.class || field.getType() == byte.class) {
            field.set(bean, Byte.parseByte(value));
        } else if (field.getType() == Boolean.class || field.getType() == boolean.class) {
            field.set(bean, Boolean.parseBoolean(value));
        }
    }

    private Object loadComponent(Class clazz, Properties prop) {
        try {
            // 1、调用无参构造器
            Object bean = clazz.getConstructor().newInstance();
            beanMap.put(clazz.getName(), bean);
            // 2、注入value值
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                // 判断属性是否有@Value注解
                if (field.isAnnotationPresent(Value.class)) {
                    Value customValue = field.getAnnotation(Value.class);
                    field.setAccessible(true);
                    String value = customValue.value();
                    // 判断类是否是配置类
                    if (clazz.isAnnotationPresent(PropertySource.class)) {
                        if (value.startsWith("${") && value.endsWith("}")) {
                            PropertySource propertySource = (PropertySource) clazz.getAnnotation(PropertySource.class);
                            Properties properties = new Properties();
                            properties.load(this.getClass().getClassLoader().getResourceAsStream(propertySource.value()));
                            value = value.substring(2, value.length() - 1);
                            String defaultValue = null;
                            if (value.contains(":")) {
                                defaultValue = value.substring(value.lastIndexOf(":") + 1);
                            }
                            field.set(bean, properties.getProperty(value, defaultValue));
                            continue;
                        }
                    }
                    if (value.startsWith("${") && value.endsWith("}")) {
                        value = value.substring(2, value.length() - 1);
                        String defaultValue = null;
                        if (value.contains(":")) {
                            defaultValue = value.substring(value.lastIndexOf(":") + 1);
                        }
                        field.set(bean, prop.getProperty(value, defaultValue));
                    }
                    setField(bean, field, value);
                }
            }

            // 初始化方法级Bean
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Bean.class)) {
                    Object result = method.invoke(bean);
                    beanMap.put(result.getClass().getName(), result);
                    log.info("注入了一个方法级Bean = " + result.getClass().getName());
                }
            }
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 装配组件
     * @param beanFactory
     */
    @Override
    protected void autowiredComponent(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        for (Object bean : beanMap.values()) {
            autowiredComponent(bean);
        }
    }

    private void autowiredComponent(Object bean) {
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Autowired.class)) {
                try {
                    field.setAccessible(true);
                    Object obj = beanFactory.getBean(field.getType());
                    if (obj == null) {
                        log.error("bean 对象不存在，type is " + field.getType().getName());
                    } else {
                        field.set(bean, obj);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 加载切面bean
     */
    @Override
    protected void loadAspect() {
        log.info("切面组件开始加载");
        // 切面--目标对象集合的映射
        Map<Object, Set<Object>> aspectMap = createAspectMap();
        // 目标对象--切面代理对象列表的映射
        Map<Object, List<Proxy>> targetMap = createTargetMap(aspectMap);

        targetMap.forEach((key, value) -> {
            Object proxy = ProxyFactory.getInstance(key, value);
            proxyMap.put(key.getClass().getName(), proxy);
        });
    }


    /**
     * 获取切面--目标对象集合的映射
     * @return
     */
    private Map<Object, Set<Object>> createAspectMap() {
        Map<Object, Set<Object>> aspectMap = new HashMap<>();
        addAspectProxy(aspectMap);
        addTransactionProxy(aspectMap);
        return aspectMap;
    }

    private void addTransactionProxy(Map<Object, Set<Object>> map) {
        Set<Object> set = null;
        for (Object obj : beanMap.values()) {
            if (obj.getClass().isAnnotationPresent(Service.class)) {
                set = new HashSet<>();
                set.add(obj);
            }
        }
        if (set != null && set.size() > 0) {
            map.put(TransactionProxy.class, set);
        }
    }

    private void addAspectProxy(Map<Object, Set<Object>> map) {
        for (Object obj : beanMap.values()) {
            // 判断类是否有Aspect注解
            if (obj.getClass().isAnnotationPresent(Aspect.class)) {
                Aspect aspect = obj.getClass().getAnnotation(Aspect.class);
                // 与该切面对应的目标对象集合
                Set<Object> objectSet = createTargetClassSet(aspect);
                map.put(obj, objectSet);
            }
        }
    }

    private Set<Object> createTargetClassSet(Aspect aspect) {
        Set<Object> targetClassSet = new HashSet<>();
        // 包名
        String packageName = aspect.packageName();
        // 类名
        String className = aspect.className();

        // 如果不为空，类完整名
        if (!"".equals(packageName) && !"".equals(className)) {
            targetClassSet.add(beanMap.get(packageName + "." + className));
        } else if (!"".equals(packageName)) {
            // 如果包名不为空, 类名为空, 则添加该包名下所有类
            targetClassSet.addAll(getSetByPackage(packageName));
        }
        return targetClassSet;
    }

    private Set<Object> getSetByPackage(String packageName) {
        Set<Object> set = new HashSet<>();
        for (String className : beanMap.keySet()) {
            if (className.startsWith(packageName)) {
                set.add(beanMap.get(className));
            }
        }
        return set;
    }

    /**
     * 获取目标对象--代理对象的映射
     * @param map
     * @return
     */
    private Map<Object, List<Proxy>> createTargetMap(Map<Object, Set<Object>> map) {
        Map<Object, List<Proxy>> targetMap = new HashMap<>();
        for (Map.Entry<Object, Set<Object>> proxyEntry : map.entrySet()) {
            Object aspectObj = proxyEntry.getKey();
            Set<Object> targetObjs = proxyEntry.getValue();
            if (aspectObj == TransactionProxy.class) {
                continue;
            }
            Proxy proxy = new AspectProxy(aspectObj);
            for (Object targetObj : targetObjs) {
                if (targetMap.containsKey(targetObj)) {
                    targetMap.get(targetObj).add(proxy);
                } else {
                    ArrayList<Proxy> aspectList = new ArrayList<>();
                    aspectList.add(proxy);
                    targetMap.put(targetObj, aspectList);
                }
            }
        }
        // 事务放在代理链最后一层
        for (Map.Entry<Object, Set<Object>> proxyEntry : map.entrySet()) {
            Object aspectObj = proxyEntry.getKey();
            Set<Object> targetObjs = proxyEntry.getValue();
            Proxy proxy;
            if (aspectObj == TransactionProxy.class) {
                proxy = new TransactionProxy(beanFactory);
                for (Object targetObj : targetObjs) {
                    if (targetMap.containsKey(targetObj)) {
                        targetMap.get(targetObj).add(proxy);
                    } else {
                        List<Proxy> aspectList = new ArrayList<Proxy>();
                        aspectList.add(proxy);
                        targetMap.put(targetObj, aspectList);
                    }
                }
            }
        }
        return targetMap;
    }

    protected void autowiredProxy() {
        beanMap.values().forEach(o -> {
            Field[] fields = o.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    autowiredProxy(field, o);
                }
            }
        });
    }

    private void autowiredProxy(Field field, Object o) {
        Class<?> type = field.getType();
        String name = null;
        for (String key : beanMap.keySet()) {
            Object obj = beanMap.get(key);
            if (obj.getClass() == type || obj.getClass().getSuperclass() == type) {
                name = key;
            } else {
                for (Class c : obj.getClass().getInterfaces()) {
                    if (c == type) {
                        name = key;
                        break;
                    }
                }
            }
        }
        if (name != null) {
            try {
                if (this.proxyMap.containsKey(name)) {
                    field.setAccessible(true);
                    field.set(o, this.proxyMap.get(name));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}