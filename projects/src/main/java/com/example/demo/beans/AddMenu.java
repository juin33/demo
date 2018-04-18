package com.example.demo.beans;

import java.io.Serializable;
import java.math.BigDecimal;

public class AddMenu implements Serializable{
    private Integer count;
    private String menuName;
    private BigDecimal price;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "AddMenu{" +
                "count=" + count +
                ", menuName='" + menuName + '\'' +
                ", price=" + price +
                '}';
    }
}
