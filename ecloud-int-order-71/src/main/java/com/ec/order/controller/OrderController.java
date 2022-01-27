package com.ec.order.controller;

import com.ec.commons.controller.BaseController;
import com.ec.commons.entities.bo.order.CreateOrderBO;
import com.ec.commons.entities.dto.order.CreateOrderDTO;
import com.ec.commons.entities.vo.order.CreateOrderVO;
import com.ec.commons.util.ret.R;
import com.ec.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController extends BaseController {

    @Resource
    private OrderService orderService;

    @PostMapping("/createOrder")
    public R createOrder(@RequestBody CreateOrderDTO dto) {
        dto.setNativeId(getNativeId());

        CreateOrderVO vo = new CreateOrderVO();
        try {

            //创建订单
            CreateOrderBO bo = orderService.createOrder(dto);

            if (bo.isSuccess()) {
                vo.setOrderNo("ED" + bo.getOrderNo().toString());
                return R.success("订单创建成功！", vo);
            }

            vo.setMsg(bo.getMsg());
        } catch (Exception ex) {
            vo.setMsg("订单创建异常。请稍后再试！" + ex.getMessage());
        }
        return R.fail(vo.getMsg());
    }

    @PostMapping("/createTXOrder")
    public R createTXOrder(@RequestBody CreateOrderDTO dto) {
        dto.setNativeId(getNativeId());

        CreateOrderVO vo = new CreateOrderVO();
        try {

            //创建订单
            CreateOrderBO bo = orderService.createTXOrder(dto);

            if (bo.isSuccess()) {
                vo.setOrderNo("ED" + bo.getOrderNo().toString());
                return R.success("分布式事务订单创建成功！", vo);
            }

            vo.setMsg(bo.getMsg());
        } catch (Exception ex) {
            vo.setMsg("分布式事务订单创建异常。请稍后再试！" + ex.getMessage());
        }
        return R.fail(vo.getMsg());
    }


    }