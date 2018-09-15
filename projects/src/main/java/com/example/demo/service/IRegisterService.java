package com.example.demo.service;

import com.example.demo.dao.Student;
import com.example.demo.support.Result;

/**
 * @author kejun
 * @date 2018/9/15 下午6:19
 */
public interface IRegisterService {
    Result<Student> register(String data);
}
