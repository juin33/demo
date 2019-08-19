package com.example.demo.design.principle;

/**
 * @author kejun
 * @date 2019/8/19 下午3:23
 * 里氏代换原则
 */
public class Test {
    public static void main(String[] args) {
        Staff staffA = new StaffImpl("A Staff");
        Staff staffB = new StaffImpl("B Staff");
        Boss bossA = new BossImpl(staffA);
        Boss bossB = new BossImpl(staffB);
        bossA.askHelp(bossB);
        Staff staffC = new StaffImpl("C Staff");
        bossA = new BossImpl(staffC);
        bossB.askHelp(bossA);
    }
}
