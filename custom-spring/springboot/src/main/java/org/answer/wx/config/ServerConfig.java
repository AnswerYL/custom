/**
 * @projectName custom-spring
 * @package org.answer.wx.config
 * @className org.answer.wx.config.ServerConfig
 */
package org.answer.wx.config;

import org.answer.wx.beans.annotation.ConfigurationProperties;

/**
 * ServerConfig
 * @description toamcat配置
 * @author answer_wx
 * @date 2022/11/8 21:27
 * @version 1.0
 */
@ConfigurationProperties(prefix = "server")
public class ServerConfig {
    private int port;

    public int getPort() {
        return port;
    }
}