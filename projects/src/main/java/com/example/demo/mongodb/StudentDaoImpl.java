package com.example.demo.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * @author kejun
 * @date 2018/11/8 下午7:52
 */

@Service
public class StudentDaoImpl implements StudentDao {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public StudentEntity findAllByName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        StudentEntity studentEntity = mongoTemplate.findOne(query,StudentEntity.class);
        return studentEntity;
    }
}
