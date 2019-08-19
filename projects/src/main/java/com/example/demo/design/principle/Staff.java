package com.example.demo.design.principle;

import com.example.demo.design.principle.Boss;

/**
 * @author kejun
 * @date 2019/8/19 下午3:14
 */
public abstract class Staff {

    private String name;

    abstract void service();
    abstract void askHelp(Boss boss);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
