package com.example.demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.example.demo.beans.AddMenu;
import com.example.demo.beans.OrderMenu;
import com.example.demo.beans.Recharge;
import com.example.demo.dao.*;
import com.example.demo.mapper.MenuMapper;
import com.example.demo.mapper.OrderMapper;
import com.example.demo.mapper.WalletMapper;
import com.example.demo.mapper.ext.MenuExtMapper;
import com.example.demo.mapper.ext.WalletExtMapper;
import com.example.demo.service.OrderService;
import com.example.demo.support.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private WalletMapper walletMapper;
    @Autowired
    private WalletExtMapper walletExtMapper;
    @Autowired
    private MenuExtMapper menuExtMapper;
    @Autowired
    private OrderMapper orderMapper;

    @Override
    public String order(String orderMenus) {
        //获取菜单
        try {
            List<OrderMenu> lists = JSON.parseArray(orderMenus, OrderMenu.class);
            if (lists != null && lists.size() > 0) {
                for (OrderMenu orderMenu : lists) {
                    Integer menuId = orderMenu.getMenuId();
                    Integer count = orderMenu.getCount();
                    Integer userId = orderMenu.getUserId();
                    MenuCriteria menuCriteria = new MenuCriteria();
                    menuCriteria.createCriteria().andIdEqualTo(menuId);
                    Menu menu = menuMapper.selectByExample(menuCriteria).get(0);
                    //获取账户余额
                    WalletCriteria walletCriteria = new WalletCriteria();
                    walletCriteria.createCriteria().andStudentIdEqualTo(userId);
                    Wallet wallet = walletMapper.selectByExample(walletCriteria).get(0);
                    //库存不足判断
                    if (null == menu || menu.getCount() <= 0) {
                        return Status.FAIL_02.getMsg();
                    }
                    //账户余额不足判断
                    if (wallet.getAmount().compareTo(menu.getPrice().multiply(new BigDecimal(count))) < 0) {
                        return Status.FAIL_01.getMsg();
                    }
                    menu.setCount(menu.getCount() - count);
                    menuMapper.updateByPrimaryKey(menu);
                    wallet.setAmount(wallet.getAmount().subtract(menu.getPrice().multiply(new BigDecimal(count))));
                    wallet.setLastUpdateDate(new Date());
                    walletMapper.updateByPrimaryKey(wallet);
                    Order order = new Order();
                    order.setMenuId(menuId);
                    order.setOrderCount(count);
                    order.setPrice(menu.getPrice());
                    order.setStudentId(userId);
                    orderMapper.insertSelective(order);
                    logger.info("下单成功,此次消费:{},数量:{},共计:{}元", menu.getMenuName(), orderMenu.getCount(), menu.getPrice().multiply(new BigDecimal(count)));
                }
                return Status.SUCCESS.getMsg();
            } else {
                return Status.SUCCESS02.getMsg();
            }
        } catch (Exception e) {
            logger.error("其他异常", e);
            return Status.Others.getMsg();
        }
    }

    @Override
    public String addMenu(String addMenus) {
        try {
            List<AddMenu> lists = JSON.parseArray(addMenus, AddMenu.class);
            if (null != lists && lists.size() > 0) {
                for (AddMenu addMenu : lists) {
                    String menuName = addMenu.getMenuName();
                    Integer count = addMenu.getCount();
                    BigDecimal price = addMenu.getPrice();
                    Menu menu = new Menu();
                    Integer menuCount = menuExtMapper.isExistOrNot(menuName);
                    if (menuCount > 0) {
                        MenuCriteria menuCriteria = new MenuCriteria();
                        menuCriteria.createCriteria().andMenuNameEqualTo(menuName);
                        menu = menuMapper.selectByExample(menuCriteria).get(0);
                        Menu otherMenu = new Menu();
                        BeanUtils.copyProperties(menu, otherMenu);
                        otherMenu.setCount(menu.getCount() + count);
                        otherMenu.setLastUpdateDate(new Date());
                        menuMapper.updateByPrimaryKey(otherMenu);
                    } else {
                        menu.setCount(count);
                        menu.setMenuName(menuName);
                        menu.setPrice(price);
                        menuMapper.insertSelective(menu);
                    }
                }
                logger.info("增加菜单成功");
                return Status.SUCCESS.getMsg();
            } else {
                return Status.SUCCESS02.getMsg();
            }
        } catch (Exception e) {
            logger.error("error", e);
            return Status.Others.getMsg();
        }
    }

    @Override
    public String recharge(String recharges) {
        try {
            List<Recharge> lists = JSON.parseArray(recharges, Recharge.class);
            if (null != lists && lists.size() > 0) {
                for (Recharge recharge : lists) {
                    Integer userId = recharge.getUserId();
                    BigDecimal amount = recharge.getAmount();
                    Wallet wallet = new Wallet();
                    Integer count = walletExtMapper.isWalletExistOrNot(userId);
                    if (count > 0) {
                        WalletCriteria walletCriteria = new WalletCriteria();
                        walletCriteria.createCriteria().andStudentIdEqualTo(userId);
                        wallet = walletMapper.selectByExample(walletCriteria).get(0);
                        Wallet newWallet = new Wallet();
                        BeanUtils.copyProperties(wallet, newWallet);
                        newWallet.setAmount(wallet.getAmount().add(amount));
                        newWallet.setLastUpdateDate(new Date());
                        walletMapper.updateByPrimaryKey(newWallet);
                    } else {
                        wallet.setStudentId(userId);
                        wallet.setAmount(amount);
                        walletMapper.insertSelective(wallet);
                    }
                }
                logger.info("充值成功");
                return Status.SUCCESS.getMsg();
            } else {
                return Status.SUCCESS02.getMsg();
            }
        } catch (Exception e) {
            logger.error("充值失败", e);
            return Status.Others.getMsg();
        }
    }
}
