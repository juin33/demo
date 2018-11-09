package com.example.demo.mongodb;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

/**
 * @author kejun
 * @date 2018/11/8 下午7:43
 */

@Document(collection = "student")
public class StudentEntity implements Serializable{

    @Id
    @Field("_id")
    private String id;

    @Field("name")
    private String name;

    @Field("age")
    private Integer age;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
