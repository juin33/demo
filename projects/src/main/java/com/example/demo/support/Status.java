package com.example.demo.support;

public enum Status {
    SUCCESS("成功"),SUCCESS02("订单为空"),FAIL_01("账户余额不足"),FAIL_02("库存不足"),Others("其他异常");
    String msg;

    public String getMsg() {
        return msg;
    }

    Status(String m){
        this.msg = m;
    }
}
