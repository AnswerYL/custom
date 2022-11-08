/**
 * @projectName custom-spring
 * @package org.answer.wx.enums
 * @className org.answer.wx.enums.IdType
 */
package org.answer.wx.enums;

/**
 * IdType
 * @description 主键生成规则
 * @author answer_wx
 * @date 2022/11/3 15:17
 * @version 1.0
 */
public enum IdType {
    /**
     * 自增
     */
    AUTO_INCREMENT(0),
    /**
     * 自定义
     */
    NONE(1),
    /**
     * 雪花算法
     */
    ID_SNOW_FLAKE(2),
    /**
     * UUID
     */
    UUID(3);

    private final int key;

    private IdType(int key) {
        this.key = key;
    }

    public int getKey() {
        return this.key;
    }
}