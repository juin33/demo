package com.example.demo.controller;

import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dr")
public class DiningController {
    @Autowired
    private OrderService orderService;

    /**
     * 增加菜单
     *
     * @param addMenus
     * @return
     */
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
}
