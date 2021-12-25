package com.ec.commons.entities.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddProductDTO {
    private String name;
    private Integer stockTotal;
    private Integer stockUsed;
    private Integer stockResidue;
}