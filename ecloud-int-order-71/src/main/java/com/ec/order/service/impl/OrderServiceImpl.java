package com.ec.order.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Assert;
import com.ec.apis.product.IProductService;
import com.ec.apis.uid.IUidGeneratorService;
import com.ec.commons.entities.bo.order.CreateOrderBO;
import com.ec.commons.entities.bo.product.ProductBO;
import com.ec.commons.entities.dto.order.CreateOrderDTO;
import com.ec.commons.entities.dto.product.DeductStockDTO;
import com.ec.commons.util.BeanUtils;
import com.ec.commons.util.StringUtilExtend;
import com.ec.order.dao.OrderDao;
import com.ec.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.shardingsphere.transaction.annotation.ShardingSphereTransactionType;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @DubboReference
    IUidGeneratorService uidGeneratorService;
    @DubboReference
    IProductService productService;

    @Resource
    OrderDao orderDao;


    /**
     * 创建订单
     */
    @Override
    public CreateOrderBO createOrder(CreateOrderDTO dto) {
        Assert.notNull(dto.getItems(), "订单明细不能为空！");
        Assert.isFalse(dto.getItems().size() == 0, "明细不能为空！");

        CreateOrderBO bo = BeanUtils.toBean(dto, CreateOrderBO.class);

        // 1. 生成orderNO
        //调用UID生成服务
        String orderNo = StringUtilExtend.Trim(uidGeneratorService.getCachedUid());
        if ("".equals(orderNo)) {
            bo.setMsg("订单号生成失败！请稍后再试！");
            return bo;
        }
        bo.setOrderNo(Convert.toLong(orderNo));

        //TODO 库存校验

        // 1. 创建主订单
        Long orderId = orderDao.createOrder(bo);
        if (orderId > 0) {
            // 2. 创建从订单
            orderDao.createOrderItems(bo);

            Object xz= productService.getProductInfoBySno("1478585211150274560");
            // 3. 扣减库存
            bo.getItems().forEach(item -> {
                DeductStockDTO deductStockDTO = new DeductStockDTO();
                deductStockDTO.setSno(item.getSno());
                deductStockDTO.setQuantity(item.getQuantity());
                productService.deductStock(deductStockDTO);
            });
            bo.setSuccess(true);
        }
        return bo;
    }

    /**
     * 创建订单（分布式事务）
     * */
    @Transactional
    @ShardingSphereTransactionType(TransactionType.BASE)
    @Override
    public CreateOrderBO createTXOrder(CreateOrderDTO dto) {

        CreateOrderBO bo = BeanUtils.toBean(dto, CreateOrderBO.class);

        // 1. 生成orderNO
        //调用UID生成服务
        String orderNo = StringUtilExtend.Trim(uidGeneratorService.getCachedUid());
        if ("".equals(orderNo)) {
            bo.setMsg("订单号生成失败！请稍后再试！");
            return bo;
        }
        bo.setOrderNo(Convert.toLong(orderNo));

        //TODO 库存校验

        // 1. 创建主订单
         Long orderId = orderDao.createOrder(bo);
        if (orderId > 0) {
            // 2. 创建从订单
            orderDao.createOrderItems(bo);

            // 3. 扣减库存
            bo.getItems().forEach(item -> {
                DeductStockDTO deductStockDTO = new DeductStockDTO();
                deductStockDTO.setSno(item.getSno());
                deductStockDTO.setQuantity(item.getQuantity());
                productService.deductStock(deductStockDTO);
            });
            bo.setSuccess(true);
        }
        return bo;
    }
}
