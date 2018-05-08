package com.example.demo.controller;

import com.example.demo.core.RetResponse;
import com.example.demo.core.RetResult;
import com.example.demo.dao.Student;
import com.example.demo.service.ILoginService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dr")
@Api(tags = {"用户操作接口"}, description = "loginController")
public class LoginController {

    @Autowired
    private ILoginService loginService;

    @ApiOperation(value = "用户登录", notes = "根据用户登录看接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "data", value = "用户登录信息", required = true,
                    dataType = "String", paramType = "query")
    })
    @RequestMapping(value = "login",method = RequestMethod.POST)
    @CrossOrigin
    public ResponseEntity<Object> login(@RequestBody String data){
        return new ResponseEntity<Object>( loginService.login(data), HttpStatus.OK);
    }

    @ApiOperation(value = "查询用户", notes = "分页查询用户所有")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "当前页码",
                    dataType = "Integer", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页显示条数",
                    dataType = "Integer", paramType = "query")
    })
    @RequestMapping(value = "selectAll",method = RequestMethod.GET)
    @CrossOrigin
    public RetResult<PageInfo<Student>> selectAll(@RequestParam(defaultValue = "0") Integer page,
                                                  @RequestParam(defaultValue = "0") Integer size) {
        PageInfo<Student> pageInfo = loginService.selectAll(page, size);
        return RetResponse.makeOKRsp(pageInfo);
    }

    @RequestMapping(value = "/selectById/{id}",method = RequestMethod.GET)
    @CrossOrigin
    public RetResult<Student> selectById(@PathVariable Integer id){
        Student student = loginService.selectById(id);
        return RetResponse.makeOKRsp(student);
    }

}
