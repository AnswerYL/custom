/**
 * @projectName custom-spring
 * @package org.answer.wx.tomcat
 * @className org.answer.wx.tomcat.ServletAndParrern
 */
package org.answer.wx.tomcat;

import org.answer.wx.web.servlet.BaseServlet;
import org.apache.log4j.Logger;

import javax.servlet.annotation.WebServlet;

/**
 * ServletAndParrern
 * @description 模式
 * @author answer_wx
 * @date 2022/11/7 21:39
 * @version 1.0
 */
public class ServletAndPattern {

    Logger log = Logger.getLogger(ServletAndPattern.class);

    private String[] patterns;
    private BaseServlet servlet;
    private String servletName;

    public String[] getPatterns() {
        return patterns;
    }

    public void setPatterns(String[] patterns) {
        this.patterns = patterns;
    }

    public BaseServlet getServlet() {
        return servlet;
    }

    public void setServlet(BaseServlet servlet) {
        this.servlet = servlet;
    }

    public String getServletName() {
        return servletName;
    }

    public void setServletName(String servletName) {
        this.servletName = servletName;
    }

    public ServletAndPattern(BaseServlet servlet) {
        this.servlet = servlet;
        if (servlet.getClass().isAnnotationPresent(WebServlet.class)) {
            WebServlet webServlet = servlet.getClass().getAnnotation(WebServlet.class);
            this.patterns = webServlet.urlPatterns();
            servletName =
                    webServlet.name().equals("") ? servletName = servlet.getClass().getName() : webServlet.name();
        } else {
            log.error(servlet.getClass() + "类找不到WebServlet注解");
        }

    }
}