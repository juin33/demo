package com.example.demo.dao;

import java.io.Serializable;
import java.util.Date;

public class Student implements Serializable {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_dining_student.Id
     *
     * @mbggenerated Wed Apr 18 15:44:21 CST 2018
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_dining_student.Name
     *
     * @mbggenerated Wed Apr 18 15:44:21 CST 2018
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_dining_student.password
     *
     * @mbggenerated Wed Apr 18 15:44:21 CST 2018
     */
    private String password;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_dining_student.Sex
     *
     * @mbggenerated Wed Apr 18 15:44:21 CST 2018
     */
    private String sex;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_dining_student.Age
     *
     * @mbggenerated Wed Apr 18 15:44:21 CST 2018
     */
    private Integer age;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_dining_student.Phone
     *
     * @mbggenerated Wed Apr 18 15:44:21 CST 2018
     */
    private String phone;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_dining_student.Createtime
     *
     * @mbggenerated Wed Apr 18 15:44:21 CST 2018
     */
    private Date createtime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column t_dining_student.LastUpdateDate
     *
     * @mbggenerated Wed Apr 18 15:44:21 CST 2018
     */
    private Date lastUpdateDate;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table t_dining_student
     *
     * @mbggenerated Wed Apr 18 15:44:21 CST 2018
     */
    private static final long serialVersionUID = 1L;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_dining_student.Id
     *
     * @return the value of t_dining_student.Id
     *
     * @mbggenerated Wed Apr 18 15:44:21 CST 2018
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_dining_student.Id
     *
     * @param id the value for t_dining_student.Id
     *
     * @mbggenerated Wed Apr 18 15:44:21 CST 2018
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_dining_student.Name
     *
     * @return the value of t_dining_student.Name
     *
     * @mbggenerated Wed Apr 18 15:44:21 CST 2018
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_dining_student.Name
     *
     * @param name the value for t_dining_student.Name
     *
     * @mbggenerated Wed Apr 18 15:44:21 CST 2018
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_dining_student.password
     *
     * @return the value of t_dining_student.password
     *
     * @mbggenerated Wed Apr 18 15:44:21 CST 2018
     */
    public String getPassword() {
        return password;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_dining_student.password
     *
     * @param password the value for t_dining_student.password
     *
     * @mbggenerated Wed Apr 18 15:44:21 CST 2018
     */
    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_dining_student.Sex
     *
     * @return the value of t_dining_student.Sex
     *
     * @mbggenerated Wed Apr 18 15:44:21 CST 2018
     */
    public String getSex() {
        return sex;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_dining_student.Sex
     *
     * @param sex the value for t_dining_student.Sex
     *
     * @mbggenerated Wed Apr 18 15:44:21 CST 2018
     */
    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_dining_student.Age
     *
     * @return the value of t_dining_student.Age
     *
     * @mbggenerated Wed Apr 18 15:44:21 CST 2018
     */
    public Integer getAge() {
        return age;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_dining_student.Age
     *
     * @param age the value for t_dining_student.Age
     *
     * @mbggenerated Wed Apr 18 15:44:21 CST 2018
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_dining_student.Phone
     *
     * @return the value of t_dining_student.Phone
     *
     * @mbggenerated Wed Apr 18 15:44:21 CST 2018
     */
    public String getPhone() {
        return phone;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_dining_student.Phone
     *
     * @param phone the value for t_dining_student.Phone
     *
     * @mbggenerated Wed Apr 18 15:44:21 CST 2018
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_dining_student.Createtime
     *
     * @return the value of t_dining_student.Createtime
     *
     * @mbggenerated Wed Apr 18 15:44:21 CST 2018
     */
    public Date getCreatetime() {
        return createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_dining_student.Createtime
     *
     * @param createtime the value for t_dining_student.Createtime
     *
     * @mbggenerated Wed Apr 18 15:44:21 CST 2018
     */
    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column t_dining_student.LastUpdateDate
     *
     * @return the value of t_dining_student.LastUpdateDate
     *
     * @mbggenerated Wed Apr 18 15:44:21 CST 2018
     */
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column t_dining_student.LastUpdateDate
     *
     * @param lastUpdateDate the value for t_dining_student.LastUpdateDate
     *
     * @mbggenerated Wed Apr 18 15:44:21 CST 2018
     */
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_dining_student
     *
     * @mbggenerated Wed Apr 18 15:44:21 CST 2018
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", password=").append(password);
        sb.append(", sex=").append(sex);
        sb.append(", age=").append(age);
        sb.append(", phone=").append(phone);
        sb.append(", createtime=").append(createtime);
        sb.append(", lastUpdateDate=").append(lastUpdateDate);
        sb.append("]");
        return sb.toString();
    }
}