package com.example.demo.controller;

import com.example.demo.service.ILoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dr")
public class LoginController {

    @Autowired
    private ILoginService loginService;

    @RequestMapping(value = "login",method = RequestMethod.POST)
    @CrossOrigin
    public ResponseEntity<Object> login(@RequestBody String data){
        return new ResponseEntity<Object>( loginService.login(data), HttpStatus.OK);
    }
}
