package com.example.demo.controller;

import com.example.demo.service.IRegisterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author kejun
 * @date 2018/9/15 下午6:36
 */

@RestController
@RequestMapping("/reg")
@Api(tags = {"用户注册接口"}, description = "registerContriller")
public class RegisterContriller {

    @Autowired
    private IRegisterService registerService;

    @ApiOperation(value = "用户注册", notes = "用户注册接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "data", value = "用户注册信息", required = true,
                    dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "add",method = RequestMethod.POST)
    @CrossOrigin
    public ResponseEntity<Object> addUser(@RequestBody String data){
        return new ResponseEntity<Object>(registerService.register(data), HttpStatus.OK);
    }
 }
