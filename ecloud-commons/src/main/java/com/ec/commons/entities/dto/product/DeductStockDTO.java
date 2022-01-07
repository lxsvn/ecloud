package com.ec.commons.entities.dto.product;

import com.ec.commons.entities.base.BaseEntity;
import lombok.Data;


@Data
public class DeductStockDTO extends BaseEntity {
    private String sno;
    private int quantity;
}
