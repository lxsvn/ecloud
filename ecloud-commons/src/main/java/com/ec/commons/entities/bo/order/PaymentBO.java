package com.ec.commons.entities.bo.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: edward
 * @Date: 2021-07-12 11:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentBO {
    private long id;
    private String serial;
}
