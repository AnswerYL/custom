/**
 * @projectName custom-spring
 * @package org.answer.wx.service
 * @className org.answer.wx.service.TestService
 */
package com.answer.wx.service;

import org.answer.wx.beans.annotation.Service;
import org.apache.log4j.Logger;

/**
 * TestService
 * @description 测试服务类
 * @author answer_wx
 * @date 2022/11/8 21:41
 * @version 1.0
 */
@Service
public class TestService {
    Logger log = Logger.getLogger(TestService.class);

    public String test() {
        log.info("controller 请求");
        return System.nanoTime() + " Hello Custom Spring Project";
    }
}