package com.example.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.controller.DiningController;
import com.example.demo.dao.Student;
import com.example.demo.dao.StudentCriteria;
import com.example.demo.mapper.StudentMapper;
import com.example.demo.service.ILoginService;
import com.example.demo.support.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginServiceImpl implements ILoginService{
    private static final Logger logger = LoggerFactory.getLogger(DiningController.class);

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public Result<Student> login(String data) {
        logger.info("用户登录:{}",data);
        Result<Student> result = Result.errorResult("登录失败!");
        Student student = JSONObject.parseObject(data,Student.class) ;
        if(null!=student){
            StudentCriteria criteria = new StudentCriteria();
            criteria.createCriteria().andPhoneEqualTo(student.getPhone());
            List<Student> lists = studentMapper.selectByExample(criteria);
            if(null==lists || lists.size()<=0){
                return  Result.errorResult("该账号未注册，请注册!");
            }
            Student student1 = lists.get(0);
            if(student1.getPassword().equals(student.getPassword())){
                return Result.successResult("登录成功", student);
            }
        }
        return result;
    }
}
