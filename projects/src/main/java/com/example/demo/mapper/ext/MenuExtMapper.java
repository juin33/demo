package com.example.demo.mapper.ext;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MenuExtMapper {
    Integer isExistOrNot(String menuName);
}