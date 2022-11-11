/**
 * @projectName custom
 * @package com.answer.wx.mapper
 * @className com.answer.wx.mapper.TestMapper
 */
package com.answer.wx.mapper;

import com.answer.wx.entity.Test;
import org.answer.wx.annotation.Delete;
import org.answer.wx.annotation.Mapper;
import org.answer.wx.annotation.Param;
import org.answer.wx.base.BaseMapper;

/**
 * TestMapper
 * @description
 * @author answer_wx
 * @date 2022/11/9 16:05
 * @version 1.0
 */
@Mapper
public interface TestMapper extends BaseMapper<Test> {
    @Delete("delete from test where id=#{id}")
    boolean delete(@Param("id") Integer id);
}