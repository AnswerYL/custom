/**
 * @projectName test
 * @package com.answer.wx.controller
 * @className com.answer.wx.controller.Test
 */
package com.answer.wx.controller;

import com.answer.wx.service.TestService;
import org.answer.wx.beans.annotation.Autowired;
import org.answer.wx.web.annotation.Controller;
import org.answer.wx.web.annotation.RequestMapping;
import org.answer.wx.web.annotation.ResponseBody;
import org.answer.wx.web.enums.RequestMethod;

/**
 * Test
 * @description 测试方法
 * @author answer_wx
 * @date 2022/11/8 17:35
 * @version 1.0
 */
@Controller
@RequestMapping("test")
public class TestController {

    @Autowired
    private TestService testService;

    @RequestMapping(value = "test", method = RequestMethod.GET)
    @ResponseBody
    public String test() {
        try {
            return testService.test();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "请求失败！";
    }
}