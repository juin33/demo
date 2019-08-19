package com.example.demo.design.principle;

/**
 * @author kejun
 * @date 2019/8/19 下午3:20
 */
public class StaffImpl extends Staff {

    public StaffImpl(String name){
        this.setName(name);
    }

    @Override
    void service() {
        System.out.println(this.getName()+" 提供服务");
    }

    @Override
    void askHelp(Boss boss) {
        boss.support();
    }
}
