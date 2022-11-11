/**
 * @projectName test
 * @package com.answer.wx.controller
 * @className com.answer.wx.controller.Test
 */
package com.answer.wx.controller;

import com.answer.wx.entity.Test;
import com.answer.wx.service.TestService;
import org.answer.wx.beans.annotation.Autowired;
import org.answer.wx.web.annotation.Controller;
import org.answer.wx.web.annotation.RequestMapping;
import org.answer.wx.web.annotation.RequestParam;
import org.answer.wx.web.annotation.ResponseBody;
import org.answer.wx.web.enums.RequestMethod;

import java.util.List;

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

    @RequestMapping(value = "testParam", method = RequestMethod.GET)
    @ResponseBody
    public String testParam(@RequestParam("i") int i) {
        try {
            return testService.test(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "请求失败！";
    }

    @RequestMapping(value = "getAll", method = RequestMethod.GET)
    @ResponseBody
    public List<Test> getAll() {
        return testService.selectAll();
    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    @ResponseBody
    public boolean delete(@RequestParam("id") int id) {
        testService.delete(id);
        return true;
    }

    @RequestMapping(value = "save", method = RequestMethod.GET)
    @ResponseBody
    public boolean save() {
        return testService.save();
    }

    @RequestMapping(value = "saveTran", method = RequestMethod.GET)
    @ResponseBody
    public boolean saveTrans() {
        return testService.saveTran();
    }

    @RequestMapping(value = "delTran", method = RequestMethod.GET)
    @ResponseBody
    public boolean delTran(@RequestParam("id") int id) {
        return testService.delTran(id);
    }

    @RequestMapping(value = "update", method = RequestMethod.GET)
    @ResponseBody
    public boolean update(@RequestParam("id") int id) {
        return testService.update(id);
    }
}