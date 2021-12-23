package com.ec.order.service.impl;

import cn.hutool.core.convert.Convert;
import com.ec.commons.entities.bo.order.CreateOrderBO;
import com.ec.commons.entities.dto.order.CreateOrderDTO;
import com.ec.commons.util.BeanUtils;
import com.ec.commons.util.StringUtilExtend;
import com.ec.order.dao.OrderDao;
import com.ec.order.service.OrderService;
import com.ec.order.service.UidGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Resource
    UidGeneratorService uidGeneratorService;
    @Resource
    OrderDao orderDao;

    /**
     * 创建订单
     */
    @Override
    public CreateOrderBO createOrder(CreateOrderDTO dto) {
        //Assert.isTrue(1 > 0, "错误");
        CreateOrderBO bo = BeanUtils.toBean(dto, CreateOrderBO.class);

        // 1. 生成orderNO
        //调用UID生成服务
        String orderNo = StringUtilExtend.Trim(uidGeneratorService.getCachedUid().getData());
        if ("".equals(orderNo)) {
            bo.setMsg("订单号生成失败！请稍后再试！");
            return bo;
        }
        bo.setOrderNo(Convert.toLong(orderNo));

        // 1. 创建主订单
        Long orderId = orderDao.createOrder(bo);
        if (orderId > 0) {
            // 2. 创建从订单
            orderDao.createOrderItems(bo);
            // 3. 扣减库存

        }
        return bo;
    }
}
