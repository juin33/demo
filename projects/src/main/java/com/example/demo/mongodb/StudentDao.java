package com.example.demo.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author kejun
 * @date 2018/11/8 下午7:49
 */
public interface StudentDao {
    StudentEntity findAllByName(String name);

}
