package com.example.demo.beans;

import java.io.Serializable;
import java.math.BigDecimal;

public class Recharge implements Serializable{
    private Integer userId;
    private BigDecimal amount;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Recharge{" +
                "userId=" + userId +
                ", amount=" + amount +
                '}';
    }
}
