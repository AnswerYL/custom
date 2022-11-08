/**
 * @projectName custom-spring
 * @package org.answer.wx.context
 * @className org.answer.wx.context.GenericApplicationContext
 */
package org.answer.wx.context;

import org.answer.wx.aop.annotation.EnableAspectJAutoProxy;
import org.answer.wx.beans.annotation.Import;
import org.answer.wx.jdbc.config.OrmConfigure;
import org.answer.wx.util.RefectUtil;
import org.answer.wx.web.annotation.SpringBootApplication;
import org.apache.log4j.Logger;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

/**
 * GenericApplicationContext
 * @description 通用application容器
 * @author answer_wx
 * @date 2022/11/4 15:59
 * @version 1.0
 */
public class GenericApplicationContext extends ActionApplicationContext implements ApplicationContext {

    Logger log = Logger.getLogger(GenericApplicationContext.class);

    protected GenericApplicationContext() {
    }

    /**
     * 加载容器，注册组件
     * @param clazz
     * @param prop
     */
    protected void loadApplicationContext(Class<?> clazz, Properties prop) {
        if (!validSpringBoot(clazz)) {
            log.error("不是一个springboot应用");
            return;
        }
        // 扫描组件
        this.scanComponent(clazz, prop);
        // 自定义import扫描组件
        this.autoImport(clazz, prop);

        // 注入组件
        super.autowiredComponent(this);

        // 启用切面
        if(clazz.isAnnotationPresent(EnableAspectJAutoProxy.class)) {
            // 加载切面、事务代理组件
            super.loadAspect();
            // 绑定代理
            super.autowiredProxy();
        }
    }

    /**
     * 判断应用是否是springboot
     * @param clazz
     * @return
     */
    private boolean validSpringBoot(Class<?> clazz) {
        return clazz.isAnnotationPresent(SpringBootApplication.class);
    }

    private void autoImport(Class<?> clazz, Properties prop) {
        Annotation[] annotations = clazz.getAnnotations();
        if (annotations.length == 0) {
            return;
        }
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().isAnnotationPresent(Import.class)) {
                Import anImport = annotation.annotationType().getAnnotation(Import.class);
                if (anImport.value().length > 0) {
                    importOrm(anImport, annotation, prop);
                }
            }
        }
    }

    private void importOrm(Import anImport, Annotation annotation, Properties prop) {
        Class<?>[] classes = anImport.value();
        for (Class<?> clazz : classes) {
            if (OrmConfigure.class.isAssignableFrom(clazz)) {
                OrmConfigure ormConfigure = RefectUtil.newInstance(clazz);
                if (ormConfigure != null) {
                    ormConfigure.auto(this, annotation, prop);
                }
            }
        }
    }


    @Override
    public Object getBean(String name) {
        return beanMap.get(name);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        return (T) beanMap.get(name);
    }

    @Override
    public Object getBean(String name, Object... args) {
        return null;
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        return getBeanByType(requiredType, true);
    }

    private <T> T getBeanByType(Class<T> requiredType, boolean bySuper) {
        Collection<Object> values = beanMap.values();
        for (Object value : values) {
            if (value.getClass() == requiredType) {
                return (T) value;
            }
        }
        if (!bySuper) {
            return null;
        }
        return getBeanbySub(requiredType);
    }

    private <T> T getBeanbySub(Class<T> requiredType) {
        for (Object o : beanMap.values()) {
            if (o.getClass().getSuperclass() == requiredType) {
                return (T) o;
            }
            for (Class<?> anInterface : o.getClass().getInterfaces()) {
                if (anInterface == requiredType) {
                    return (T) o;
                }
            }
        }
        return null;
    }

    @Override
    public <T> T getBean(Class<T> requiredType, Object... args) {
        return null;
    }

    @Override
    public boolean containsBean(String name) {
        return beanMap.containsKey(name);
    }

    @Override
    public boolean isSingleton(String name) {
        return false;
    }

    @Override
    public boolean isPrototype(String name) {
        return false;
    }

    @Override
    public boolean isTypeMatch(String name, Class<?> typeToMatch) {
        return false;
    }

    @Override
    public Class<?> getType(String name) {
        return null;
    }

    @Override
    public String[] getAliases(String name) {
        return new String[0];
    }

    @Override
    public boolean register(String name, Object bean) {
        return beanMap.put(name, bean) == null;
    }

    @Override
    public boolean remove(String name) {
        return beanMap.remove(name) != null;
    }

    @Override
    public boolean remove(Class<?> requiredType) {
        return false;
    }

    @Override
    public Map<String, Object> getIOC() {
        return beanMap;
    }
}