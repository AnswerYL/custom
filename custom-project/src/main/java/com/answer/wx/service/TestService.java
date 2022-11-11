/**
 * @projectName custom-spring
 * @package org.answer.wx.service
 * @className org.answer.wx.service.TestService
 */
package com.answer.wx.service;

import com.answer.wx.entity.Test;
import org.answer.wx.beans.annotation.Service;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * TestService
 * @description test服务接口
 * @author answer_wx
 * @date 2022/11/8 21:41
 * @version 1.0
 */
public interface TestService {
    String test();

    String test(int i);

    List<Test> selectAll();

    void delete(int id);

    boolean save();

    // 添加 事务
    boolean saveTran();

    boolean update(int id);

    // 删除事务
    boolean delTran(int id);
}