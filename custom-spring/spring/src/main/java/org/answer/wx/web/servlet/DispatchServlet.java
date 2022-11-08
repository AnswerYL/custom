/**
 * @projectName custom-spring
 * @package org.answer.wx.web.servlet
 * @className org.answer.wx.web.servlet.DispatchServlet
 */
package org.answer.wx.web.servlet;


import org.answer.wx.context.AnnotationConfigApplicationContext;
import org.answer.wx.handler.HandlerMapping;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

/**
 * DispatchServlet
 * @description 分发器
 * @author answer_wx
 * @date 2022/11/7 21:58
 * @version 1.0
 */
@WebServlet(urlPatterns = "/*")
public class DispatchServlet extends HttpServlet implements BaseServlet {

    Logger log = Logger.getLogger(DispatchServlet.class);

    private Class<?> clazz;
    private Properties properties;

    public DispatchServlet(Class<?> clazz, Properties properties) {
        this.clazz = clazz;
        this.properties = properties;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HandlerMapping handlerMapping = new HandlerMapping();
        handlerMapping.request(req, resp);
    }

    @Override
    public void loadOnStartup() {
        log.info("Spring 开始加载...");
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(clazz, properties);
        log.info(context.getIOC());
        log.info("----------");
        log.info(context.getProxyMap());
        log.info("IOC容器加载完毕！");
    }
}