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
    private StringBuilder PREFIX = new StringBuilder("jdbc.");
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
        StringBuilder url = PREFIX.append("url");
        StringBuilder username = PREFIX.append("username");
        StringBuilder password = PREFIX.append("password");
        StringBuilder driver = PREFIX.append("driver");
        if (properties.contains(url)) {
            this.url = this.properties.getProperty(url.toString());
        } else {
            log.error("配置文件中找不到" + url.toString() + "属性");
        }
        if (properties.contains(username)) {
            this.username = this.properties.getProperty(username.toString());
        } else {
            log.error("配置文件中找不到" + username.toString() + "属性");
        }
        if (properties.contains(password)) {
            this.password = this.properties.getProperty(password.toString());
        } else {
            log.error("配置文件中找不到" + password.toString() + "属性");
        }
        if (properties.contains(driver)) {
            this.driver = this.properties.getProperty(driver.toString());
        } else {
            log.error("配置文件中找不到" + driver.toString() + "属性");
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