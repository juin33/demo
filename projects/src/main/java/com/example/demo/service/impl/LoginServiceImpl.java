package com.example.demo.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.controller.DiningController;
import com.example.demo.dao.Student;
import com.example.demo.dao.StudentCriteria;
import com.example.demo.exception.ServiceException;
import com.example.demo.mapper.StudentMapper;
import com.example.demo.mapper.ext.StudentExtMapper;
import com.example.demo.service.ILoginService;
import com.example.demo.support.Result;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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

    @Autowired
    private StudentExtMapper studentExtMapper;

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

    @Override
    public PageInfo<Student> selectAll(Integer page, Integer size,String msg,Student student) {
        //开启分页查询，写在查询语句上方
        //只有紧跟在PageHelper.startPage方法后的第一个Mybatis的查询（Select）方法会被分页。
        PageHelper.startPage(page, size);
        List<Student> userInfoList = studentExtMapper.selectAll();
        PageInfo<Student> pageInfo = new PageInfo<>(userInfoList);
        return pageInfo;
    }

    @Override
    public Student selectById(Integer id) {
        Student student = studentMapper.selectByPrimaryKey(id);
        if(null == student){
            throw new ServiceException("用户表不存在该用户");
        }
        return student;
    }

}
