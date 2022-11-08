/**
 * @projectName custom-spring
 * @package org.answer.wx.tomcat
 * @className org.answer.wx.tomcat.TomcatServer
 */
package org.answer.wx.tomcat;

import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServlet;
import java.util.List;

/**
 * TomcatServer
 * @description Tomcat服务
 * @author answer_wx
 * @date 2022/11/7 21:36
 * @version 1.0
 */
public class TomcatServer {

    static Logger log = Logger.getLogger(TomcatServer.class);

    public static void start(String contextPath, List<ServletAndPattern> list, int port) {
        // 创建tomcat服务器
        Tomcat server = new Tomcat();
        // 指定端口号
        server.setPort(port);
        // 是否自动部署
        server.getHost().setAutoDeploy(false);
        // 创建上下文
        StandardContext context = new StandardContext();
        context.setPath(contextPath);
        // 监听上下文
        context.addLifecycleListener(new Tomcat.FixContextListener());
        // 上下文添加到服务中
        server.getHost().addChild(context);
        for (ServletAndPattern tmp : list) {
            // 创建Servlet
            server.addServlet(contextPath, tmp.getServletName(), (HttpServlet) tmp.getServlet());
            // Servlet映射
            String[] mapping = tmp.getPatterns();
            for (String url : mapping) {
                context.addServletMappingDecoded(url, tmp.getServletName());
            }
            tmp.getServlet().loadOnStartup();
        }
        // 启动服务
        try {
            server.start();
            log.info("tomcat" + port + "启动完成...");
            // 异步接收请求
            server.getServer().await();
        } catch (Exception e) {
            log.error("tomcat启动出现错误..." + e.getMessage());
            e.printStackTrace();
        }
    }
}