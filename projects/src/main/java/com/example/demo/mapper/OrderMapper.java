package com.example.demo.mapper;

import com.example.demo.dao.Order;
import com.example.demo.dao.OrderCriteria;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface OrderMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_dining_order
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    int countByExample(OrderCriteria example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_dining_order
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    int deleteByExample(OrderCriteria example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_dining_order
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_dining_order
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    int insert(Order record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_dining_order
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    int insertSelective(Order record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_dining_order
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    List<Order> selectByExampleWithRowbounds(OrderCriteria example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_dining_order
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    List<Order> selectByExample(OrderCriteria example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_dining_order
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    Order selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_dining_order
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    int updateByExampleSelective(@Param("record") Order record, @Param("example") OrderCriteria example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_dining_order
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    int updateByExample(@Param("record") Order record, @Param("example") OrderCriteria example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_dining_order
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    int updateByPrimaryKeySelective(Order record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_dining_order
     *
     * @mbggenerated Fri Mar 30 10:47:23 CST 2018
     */
    int updateByPrimaryKey(Order record);
}