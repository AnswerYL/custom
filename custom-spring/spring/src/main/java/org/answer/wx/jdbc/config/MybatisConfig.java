/**
 * @projectName custom-spring
 * @package org.answer.wx.config
 * @className org.answer.wx.config.MybatisConfig
 */
package org.answer.wx.jdbc.config;

import org.answer.wx.annotation.Mapper;
import org.answer.wx.beans.annotation.MapperScan;
import org.answer.wx.context.ActionApplicationContext;
import org.answer.wx.context.ApplicationContext;
import org.answer.wx.jdbc.session.DefaultSqlSessionFactory;
import org.answer.wx.jdbc.session.SqlSessionFactory;
import org.answer.wx.proxy.MapperProxyFactory;
import org.answer.wx.session.SqlConfig;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * MybatisConfig
 * @description Mybatis配置类
 * @author answer_wx
 * @date 2022/11/4 17:01
 * @version 1.0
 */
public class MybatisConfig implements OrmConfigure {

    private ApplicationContext applicationContext;

    @Override
    public void auto(ApplicationContext context, Annotation annotation, Properties prop) {
        this.applicationContext = context;
        MapperScan mapperScan = (MapperScan) annotation;
        applicationContext.getIOC().put(SqlSessionFactory.class.getName(), new DefaultSqlSessionFactory(new SqlConfig(prop)));
        String[] packages = mapperScan.basePackages();
        for (String pkg : packages) {
            loadComponent(pkg);
        }
    }

    public void loadComponent(String basePackage) {
        try {
            String path = basePackage.replaceAll("\\.", "/");
            Enumeration<URL> resources = this.getClass().getClassLoader().getResources(path);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                if (url == null) {
                    continue;
                }
                loadComponent(basePackage, url);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadComponent(String basePackage, URL url) {
        String protocol = url.getProtocol();
        if ("file".equalsIgnoreCase(protocol)) {
            String path = url.getPath().replaceAll("%20", " ");
            loadComponentByPath(basePackage, path);
        }
        if ("jar".equalsIgnoreCase(protocol)) {
            loadCompenentByJar(url);
        }
    }

    private void loadCompenentByJar(URL url) {
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
                    loadComponentByClazz(className);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadComponentByPath(String basePackage, String path) {
        File[] files = new File(path).listFiles(file -> (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory());
        assert files != null;
        for (File file : files) {
            String fileName = file.getName();
            if (file.isDirectory()) {
                String childPath = path + ActionApplicationContext.SEPARATE + fileName;
                loadComponentByPath(basePackage + "." + fileName, childPath);
            } else {
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                String packageName = path.replaceAll("\\\\", ".").replaceAll("/", ".");
                className = packageName + "." + className;
                loadComponentByClazz(className);
            }
        }
    }

    private void loadComponentByClazz(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            if (clazz.isAnnotationPresent(Mapper.class)) {
                loadComponent(clazz);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadComponent(Class<?> clazz) {
        Object obj = MapperProxyFactory.getProxy(clazz);
        applicationContext.getIOC().put(clazz.getName(), obj);
    }


}