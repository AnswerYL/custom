/**
 * @projectName custom
 * @package com.answer.wx.entity
 * @className com.answer.wx.entity.Test
 */
package com.answer.wx.entity;

import org.answer.wx.annotation.Column;
import org.answer.wx.annotation.Id;
import org.answer.wx.annotation.Table;

/**
 * Test
 * @description 测试表
 * @author answer_wx
 * @date 2022/11/9 16:02
 * @version 1.0
 */
@Table(name = "test")
public class Test {
    @Id
    @Column(value = "id")
    private Integer id;
    @Column(value = "`name`")
    private String name;
    @Column(value = "`desc`")
    private String desc;
    @Column(value = "`status`")
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", desc='" + desc + '\'' +
                ", status=" + status +
                '}';
    }
}