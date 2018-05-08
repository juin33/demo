package com.example.demo.controller;

import com.example.demo.dao.Student;
import com.example.demo.service.ILoginService;
import com.example.demo.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dr")
@Api(tags = {"用户订单接口"}, description = "DiningController")
public class DiningController{
    @Autowired
    private OrderService orderService;
    @Autowired
    private ILoginService loginService;

   private static final Logger logger = LoggerFactory.getLogger(DiningController.class);

    /**
     * 增加菜单
     *
     * @param addMenus
     * @return
     */
    @ApiOperation(value = "增加菜单", notes = "增加菜单接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "data", value = "增加菜单", required = true,
                    dataType = "String", paramType = "add")
    })
    @RequestMapping(value = "/addMenu", method = RequestMethod.POST)
    public String addMenu(@RequestParam(value = "addMenus") String addMenus) {
        return orderService.addMenu(addMenus);
    }

    /**
     * 钱包充值
     *
     * @param recharges
     * @return
     */
    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    public String recharge(@RequestParam(value = "recharges") String recharges) {
        return orderService.recharge(recharges);
    }

    /**
     * 下单
     *
     * @param orderMenus
     * @return
     */
    @RequestMapping(value = "/order", method = RequestMethod.POST)
    public String order(@RequestParam(value = "orderMenus") String orderMenus) {
        return orderService.order(orderMenus);
    }



    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String hello(){
        return "success";
    }
}
