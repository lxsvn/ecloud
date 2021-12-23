package com.ec.commons.entities.po.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: edward
 * @Date:  2021年12月21日
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentPO {
    private long id;
    private String serial;
}
