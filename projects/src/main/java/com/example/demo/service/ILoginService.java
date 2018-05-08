package com.example.demo.service;

import com.example.demo.dao.Student;
import com.example.demo.support.Result;
import com.github.pagehelper.PageInfo;

public interface ILoginService {
   Result<Student> login(String data);

   PageInfo<Student> selectAll(Integer page, Integer size);
}
