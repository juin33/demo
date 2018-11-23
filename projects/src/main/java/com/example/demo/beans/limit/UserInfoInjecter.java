package com.example.demo.beans.limit;

import com.example.demo.dao.Student;
import site.higgs.limiter.interceptor.ArgumentInjecter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author kejun
 * @date 2018/11/23 下午3:44
 */
public class UserInfoInjecter implements ArgumentInjecter {
    @Override
    public Map<String, Object> inject(Object... objects) {
        /**
         * 大多数项目中 当前登录用户都是存放在线程级变量中
         */
        Student student = new Student();
        student.setName("juin");
        student.setPhone("18368493590");
        Map<String, Object> retVal = new HashMap<>();
        retVal.put("student", student);
        return retVal;
    }
}
