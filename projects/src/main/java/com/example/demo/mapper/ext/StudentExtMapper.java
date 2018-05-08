package com.example.demo.mapper.ext;

import com.example.demo.dao.Student;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StudentExtMapper {
    List<Student> selectAll();
}
