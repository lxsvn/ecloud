package com.ec.commons.entities.dto.order;

import lombok.Data;

@Data
public class CreateOrderItemDTO {
    private int pid;
    private int quantity;
}
