package com.example.demo.controller;

import com.example.demo.core.RetResponse;
import com.example.demo.core.RetResult;
import com.example.demo.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/redis")
public class RedisController {
    @Autowired
    private RedisService redisService;
    @RequestMapping(value = "/setRedis/{name}",method = RequestMethod.GET)
    public RetResult<String> setRedis(@PathVariable String name) {
        redisService.set("name",name);
        return RetResponse.makeOKRsp(name);
    }

    @RequestMapping(value="/getRedis",method = RequestMethod.GET)
    public RetResult<String> getRedis() {
        String name = redisService.get("name");
        return RetResponse.makeOKRsp(name);
    }

}
