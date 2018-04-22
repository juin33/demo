package com.example.demo.service;

import com.example.demo.dao.Student;
import com.example.demo.support.Result;

public interface ILoginService {
   Result<Student> login(String data);
}
