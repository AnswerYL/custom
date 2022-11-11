/**
 * @projectName custom-spring
 * @package org.answer.wx.session
 * @className org.answer.wx.session.SqlConfig
 */
package org.answer.wx.session;

import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Properties;

/**
 * SqlConfig
 * @description sql配置
 * @author answer_wx
 * @date 2022/11/4 17:21
 * @version 1.0
 */
public class SqlConfig {
    Logger log = Logger.getLogger(SqlConfig.class);

    private Properties properties;
    private String PREFIX = "jdbc.";
    public String url;
    public String username;
    public String password;
    public String driver;
    private boolean update = false;

    public SqlConfig(Properties properties) {
        this.properties = properties;
        // 1、校验配置文件正确性
        validProperties(properties);
        // 2、加载数据库驱动
        loadDriver();
    }

    private void validProperties(Properties properties) {
        String url = PREFIX.concat("url");
        String username = PREFIX.concat("username");
        String password = PREFIX.concat("password");
        String driver = PREFIX.concat("driver");
        if (properties.containsKey(url)) {
            this.url = this.properties.getProperty(url);
        } else {
            log.error("配置文件中找不到" + url + "属性");
        }
        if (properties.containsKey(username)) {
            this.username = this.properties.getProperty(username);
        } else {
            log.error("配置文件中找不到" + username + "属性");
        }
        if (properties.containsKey(password)) {
            this.password = this.properties.getProperty(password);
        } else {
            log.error("配置文件中找不到" + password + "属性");
        }
        if (properties.containsKey(driver)) {
            this.driver = this.properties.getProperty(driver);
        } else {
            log.error("配置文件中找不到" + driver + "属性");
        }
    }

    private void loadDriver() {
        try {
            Class.forName(this.driver);
            log.info("加载jdbc驱动成功");
        } catch (ClassNotFoundException e) {
            log.error("找不到jdbc驱动");
        }
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }
}