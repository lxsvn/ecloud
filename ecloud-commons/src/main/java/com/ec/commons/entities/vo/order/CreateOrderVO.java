package com.ec.commons.entities.vo.order;

import lombok.Data;

@Data
public class CreateOrderVO {
    private boolean isSuccess;
    private String orderNo;
    private String msg;
}
