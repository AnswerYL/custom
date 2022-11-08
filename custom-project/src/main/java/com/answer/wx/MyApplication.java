/**
 * @projectName test
 * @package com.answer.wx
 * @className com.answer.wx.MyApplication
 */
package com.answer.wx;

import org.answer.wx.SpringApplication;
import org.answer.wx.beans.annotation.ComponentScan;
import org.answer.wx.web.annotation.SpringBootApplication;

/**
 * MyApplication
 * @description 测试项目入口
 * @author answer_wx
 * @date 2022/11/8 17:34
 * @version 1.0
 */
@SpringBootApplication
@ComponentScan("com.answer.wx")
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}