package com.ec.commons.entities.bo.order;

import lombok.Data;

@Data
public class CreateOrderItemBO {
    private String sno;
    private int quantity;
    private double unitPrice;
}
