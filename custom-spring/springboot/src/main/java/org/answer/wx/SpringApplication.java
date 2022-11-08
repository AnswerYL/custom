/**
 * @projectName custom-spring
 * @package org.answer.wx
 * @className org.answer.wx.SpringApplication
 */
package org.answer.wx;

import org.answer.wx.beans.annotation.Autowired;
import org.answer.wx.config.ServerConfig;
import org.answer.wx.tomcat.ServletAndPattern;
import org.answer.wx.tomcat.TomcatServer;
import org.answer.wx.util.FilePath;
import org.answer.wx.web.servlet.DispatchServlet;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * SpringApplication
 * @description
 * @author answer_wx
 * @date 2022/11/7 21:51
 * @version 1.0
 */
public class SpringApplication {
    static Logger log = Logger.getLogger(SpringApplication.class);

    @Autowired
    private ServerConfig serverConfig;

    public static void run(Class<?> clazz, String[] args) {
        long start = System.currentTimeMillis();
        // 获取配置文件
        Properties properties = new Properties();
        File file = new File(FilePath.RESOURCES_PATH + "application.properties");
        if (!file.exists()) {
            log.error("找不到springboot项目配置文件");
            return;
        }
        try {
            properties.load(new FileInputStream(file));
        } catch (Exception e) {
            log.error("读取springboot项目配置文件出错：" + e.getMessage());
            e.printStackTrace();
        }
        run(properties, clazz);
        long end = System.currentTimeMillis();
        log.info("项目启动成功耗时：" + (end - start));
    }

    private static void run(Properties properties, Class<?> clazz) {
        ServletAndPattern dispatchServlet = new ServletAndPattern(new DispatchServlet(clazz, properties));
        List<ServletAndPattern> list = new ArrayList<>();
        list.add(dispatchServlet);
        int port = 8080;
        if (properties.containsKey("server.port")) {
            try {
                port = Integer.parseInt(properties.getProperty("server.port"));
            } catch (Exception e) {
                log.error("端口号配置错误");
                e.printStackTrace();
            }
        }
        // 启动servlet容器
        TomcatServer.start("", list, port);
    }
}