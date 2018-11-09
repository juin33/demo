package com.example.demo.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author kejun
 * @date 2018/11/9 上午10:13
 */
public interface StudentRepository extends MongoRepository<StudentEntity,String> {
    public StudentEntity findByName(String name);
    public List<StudentEntity> findAll();
}
