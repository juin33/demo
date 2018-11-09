package com.example.demo.mongodb;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author kejun
 * @date 2018/11/8 下午7:56
 */

@RestController
@RequestMapping("/stu")
public class StudentController {
    @Autowired
    private StudentDao studentDao;
    @Autowired
    private StudentRepository studentRepository;


    @RequestMapping(value = "/find/{name}",method = RequestMethod.GET)
    public ResponseEntity<Object> findOne(@PathVariable String name){
        StudentEntity studentEntity = studentDao.findAllByName(name);
        return new ResponseEntity<Object>("success"+ JSON.toJSONString(studentEntity), HttpStatus.OK);
    }

    @RequestMapping(value = "/find/ext/{name}",method = RequestMethod.GET)
    public ResponseEntity<Object> findByName(@PathVariable String name){
        StudentEntity studentEntity = studentRepository.findByName(name);
        List<StudentEntity> students = studentRepository.findAll();
        return new ResponseEntity<Object>("success"+ JSON.toJSONString(studentEntity)+ JSONArray.toJSONString(students), HttpStatus.OK);
    }
}
