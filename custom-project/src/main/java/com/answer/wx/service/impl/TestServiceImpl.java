/**
 * @projectName custom
 * @package com.answer.wx.service.impl
 * @className com.answer.wx.service.impl.TestServiceImpl
 */
package com.answer.wx.service.impl;

import com.answer.wx.entity.Test;
import com.answer.wx.mapper.TestMapper;
import com.answer.wx.service.TestService;
import org.answer.wx.beans.annotation.Autowired;
import org.answer.wx.beans.annotation.Service;
import org.answer.wx.transaction.Transactional;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * TestServiceImpl
 * @description testService实现类
 * @author answer_wx
 * @date 2022/11/9 16:14
 * @version 1.0
 */
@Service
public class TestServiceImpl implements TestService {

    Logger log = Logger.getLogger(TestServiceImpl.class);

    @Autowired
    private TestMapper testMapper;

    /**
     * 判断是否调用成功
     * @return
     */
    @Override
    public String test() {
        return System.currentTimeMillis() + " Hello Custom Project";
    }

    /**
     * 判断参数是否成功
     * @param i
     * @return
     */
    @Override
    public String test(int i) {
        return System.currentTimeMillis() + " params：" + i;
    }

    @Override
    @Transactional
    public List<Test> selectAll() {
        return testMapper.getAll();
    }

    @Override
    public void delete(int id) {
        testMapper.delete(id);
    }

    @Override
    public boolean save() {
        Test test = new Test();
//        test.setId(3L);
        test.setName("answer");
        test.setDesc("desc answer");
        test.setStatus(1);
        boolean ret = testMapper.save(test);
        if (!ret) {
            throw new RuntimeException("添加数据失败");
        }
        return ret;
    }

    /**
     * 强制报错
     * @return
     */
    @Override
    @Transactional
    public boolean saveTran() {
        Test test = new Test();
        test.setName("answer-1");
        test.setDesc("desc answer-1");
        test.setStatus(1);
        boolean ret = testMapper.save(test);
        if (ret) {
            throw new RuntimeException("transaction 添加数据失败");
        }
        return false;
    }

    @Override
    public boolean update(int id) {
        Test test = testMapper.getById(id);
        if (test == null) {
            return false;
        }
        test.setDesc(System.currentTimeMillis() + " " + test.getName());
        return testMapper.update(test);
    }

    @Override
    @Transactional
    public boolean delTran(int id) {
        int ret = testMapper.delete(id);
        if (ret > 0) {
            throw new RuntimeException("transaction 删除失败");
        }
        return false;
    }
}