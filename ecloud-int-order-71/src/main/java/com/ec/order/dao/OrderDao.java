package com.ec.order.dao;


import com.ec.commons.entities.bo.order.CreateOrderBO;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface OrderDao {

    /**
     * 创建订单主表
     * */
    Long createOrder(CreateOrderBO bo);

    /**
     * 创建订单从表
     * */
    void createOrderItems(CreateOrderBO bo);
}
