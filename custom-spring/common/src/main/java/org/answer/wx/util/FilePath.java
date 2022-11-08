/**
 * @projectName custom-spring
 * @package org.answer.wx.util
 * @className org.answer.wx.util.FilePath
 */
package org.answer.wx.util;

import java.io.File;

/**
 * FilePath
 * @description 文件路径
 * @author answer_wx
 * @date 2022/11/8 15:24
 * @version 1.0
 */
public class FilePath {
    public static final String DIR = File.separator;
    public static final String RESOURCES_PATH = "src" + DIR + "main" + DIR + "resources" + DIR;
    public static final String TEMPLATES_PATH = RESOURCES_PATH + "templates";
    public static final String STATIC_PATH = RESOURCES_PATH + "static";
}