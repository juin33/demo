package com.example.demo.design.principle;

/**
 * @author kejun
 * @date 2019/8/19 下午3:18
 * 里氏代换原则
 */
public class BossImpl extends Boss{

    public BossImpl(Staff staff) {
        super(staff);
    }

    @Override
    void support() {
        this.getStaff().service();
    }

    @Override
    void askHelp(Boss boss) {
        boss.support();
    }
}
