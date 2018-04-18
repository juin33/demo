package com.example.demo.beans;

import java.io.Serializable;

public class OrderMenu implements Serializable{
    private Integer userId;
    private Integer menuId;
    private Integer count;

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "{" +
                "userId=" + userId +
                ", menuId=" + menuId +
                ", count=" + count +
                '}';
    }
}
