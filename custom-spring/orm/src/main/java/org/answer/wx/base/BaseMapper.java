/**
 * @projectName custom-spring
 * @package org.answer.wx.base
 * @className org.answer.wx.base.BaseMapper
 */
package org.answer.wx.base;

import java.io.Serializable;
import java.util.List;

/**
 * BaseMapper
 * @description 基础Mapper接口
 * @author answer_wx
 * @date 2022/11/4 17:57
 * @version 1.0
 */
public interface BaseMapper<T> {
    boolean saveOrUpdate(T object);

    T getById(Serializable id);

    boolean save(T object);

    boolean update(T object);

    List<T> getAll();

    boolean delete(Serializable id);

    List<T> getAllByPage(int start, int end);

    int getCount();

    void close();

    void flush();
}