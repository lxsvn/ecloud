package com.ec.order.service;

import com.ec.commons.entities.bo.order.CreateOrderBO;
import com.ec.commons.entities.dto.order.CreateOrderDTO;
import org.springframework.stereotype.Service;


@Service
public interface OrderService {
    /**
     * 创建订单
     * */
    CreateOrderBO createOrder(CreateOrderDTO bo);

    /**
     * 创建订单（分布式事务）
     * */
     CreateOrderBO createTXOrder(CreateOrderDTO dto);
 }
