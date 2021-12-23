package com.ec.commons.entities.bo.order;

import lombok.Data;

@Data
public class CreateOrderItemBO {
    private int pid;
    private int quantity;
    private double unitPrice;
}
