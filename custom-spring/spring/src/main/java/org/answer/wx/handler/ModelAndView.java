/**
 * @projectName custom-spring
 * @package org.answer.wx.handler
 * @className org.answer.wx.handler.ModelAndView
 */
package org.answer.wx.handler;

import java.util.HashMap;

/**
 * ModelAndView
 * @description 视图
 * @author answer_wx
 * @date 2022/11/8 14:43
 * @version 1.0
 */
public class ModelAndView extends HashMap<String, Object> {
    private String viewName;

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public ModelAndView(String viewName) {
        this.viewName = viewName;
    }

    /**
     * 默认构造器，调用有参构造
     */
    public ModelAndView() {
        this("");
    }

    public boolean addObject(String key, Object value) {
        return super.put(key, value) == null;
    }
}