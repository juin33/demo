package com.example.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.Student;
import com.example.demo.dao.StudentCriteria;
import com.example.demo.mapper.StudentMapper;
import com.example.demo.service.IRegisterService;
import com.example.demo.support.Result;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author kejun
 * @date 2018/9/15 下午6:21
 */

@Service
public class RegisterServiceImpl implements IRegisterService {
    private static final Logger logger = LoggerFactory.getLogger(RegisterServiceImpl.class);

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public Result<Student> register(String data) {
        Result<Student> result = null;
        if(Strings.isNullOrEmpty(data)){
            result = Result.errorResult("注册信息为空");
        }
        Student student = JSONObject.parseObject(data,Student.class);
        if(checkRegisterInfo(student)){
            logger.info("注册信息完整，开始验证账号是否可用");
            if(isAccessed(student)){
                studentMapper.insertSelective(student);
                logger.info("新用户注册成功,phone:{}",student.getPhone());
                result = Result.successResult("新用户注册成功",student);
            }
        }
        return result;
    }

    public Boolean checkRegisterInfo(Student student){
        if(!Strings.isNullOrEmpty(student.getPhone()) && !Strings.isNullOrEmpty(student.getPassword())){
            return true;
        }
        return false;
    }

    public Boolean isAccessed(Student student){
        StudentCriteria criteria = new StudentCriteria();
        criteria.createCriteria().andPhoneEqualTo(student.getPassword());
        criteria.setOrderByClause("lastUpdateDate desc");
        List<Student> students = studentMapper.selectByExample(criteria);
        if(null == students || students.size()<=0){
            return true;
        }
        return false;
    }
}
