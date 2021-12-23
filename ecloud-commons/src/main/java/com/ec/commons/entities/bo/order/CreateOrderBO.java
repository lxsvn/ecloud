package com.ec.commons.entities.bo.order;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderBO {
    private boolean isSuccess;
    private String msg = "";

    private Long id;
    /**
     * 通过 uid-generator 服务生成
     */
    private Long orderNo;

    /**
     * 用户唯一标识
     */
    private String nativeId;

    /**
     * 订单明细
     */
    private List<CreateOrderItemBO> items;
}
