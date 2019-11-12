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
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;

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

    @RequestMapping("/login1")
    public String login(HttpSession session, HttpServletRequest request) {
        String id = request.getParameter("id");
        String name = request.getParameter("name");
        HashMap<Object, Object> userInfo = new HashMap<>(16);
        userInfo.put("id", id);
        userInfo.put("name", name);
        session.setAttribute("USER_INFO", userInfo);
        return userInfo + "  成功存储到会话中";
    }

    @RequestMapping("/getUserInfo")
    public String getUserInfo(HttpSession session, HttpServletRequest request) {
        Object user_info = session.getAttribute("USER_INFO");
        if (user_info == null) {
            return "请先登录,再读取会话数据";
        }
        return "从会话中读取数据 " + user_info;
    }

    /**
     * 无参数的重定向
     * @return
     */
    @RequestMapping("/redirect/noparams")
    public RedirectView redirectView(){
        RedirectView redirectView = new RedirectView();
        redirectView.setContextRelative(true);
        redirectView.setUrl("http://www.baidu.com");
        return redirectView;
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
        Student student = new Student();
        student.setId(1);
        student.setName("tom");
        PageInfo<Student> pageInfo = loginService.selectAll(page, size,"123456",student);
        return RetResponse.makeOKRsp(pageInfo);
    }

    @RequestMapping(value = "/selectById/{id}",method = RequestMethod.GET)
    @CrossOrigin
    public RetResult<Student> selectById(@PathVariable Integer id){
        Student student = loginService.selectById(id);
        return RetResponse.makeOKRsp(student);
    }

}
