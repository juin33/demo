package com.example.demo.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class Order implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_dining_order.Id
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_dining_order.StudentId
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    private Integer studentId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_dining_order.Price
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    private BigDecimal price;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_dining_order.MenuId
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    private Integer menuId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_dining_order.OrderCount
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    private Integer orderCount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_dining_order.Createtime
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    private Date createtime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_dining_order.LastUpdateDate
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    private Date lastUpdateDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_dining_order
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_dining_order.Id
     *
     * @return the value of t_dining_order.Id
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_dining_order.Id
     *
     * @param id the value for t_dining_order.Id
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_dining_order.StudentId
     *
     * @return the value of t_dining_order.StudentId
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    public Integer getStudentId() {
        return studentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_dining_order.StudentId
     *
     * @param studentId the value for t_dining_order.StudentId
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_dining_order.Price
     *
     * @return the value of t_dining_order.Price
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_dining_order.Price
     *
     * @param price the value for t_dining_order.Price
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_dining_order.MenuId
     *
     * @return the value of t_dining_order.MenuId
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    public Integer getMenuId() {
        return menuId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_dining_order.MenuId
     *
     * @param menuId the value for t_dining_order.MenuId
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_dining_order.OrderCount
     *
     * @return the value of t_dining_order.OrderCount
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    public Integer getOrderCount() {
        return orderCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_dining_order.OrderCount
     *
     * @param orderCount the value for t_dining_order.OrderCount
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_dining_order.Createtime
     *
     * @return the value of t_dining_order.Createtime
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_dining_order.Createtime
     *
     * @param createtime the value for t_dining_order.Createtime
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_dining_order.LastUpdateDate
     *
     * @return the value of t_dining_order.LastUpdateDate
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_dining_order.LastUpdateDate
     *
     * @param lastUpdateDate the value for t_dining_order.LastUpdateDate
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_dining_order
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", studentId=").append(studentId);
        sb.append(", price=").append(price);
        sb.append(", menuId=").append(menuId);
        sb.append(", orderCount=").append(orderCount);
        sb.append(", createtime=").append(createtime);
        sb.append(", lastUpdateDate=").append(lastUpdateDate);
        sb.append("]");
        return sb.toString();
    }
}