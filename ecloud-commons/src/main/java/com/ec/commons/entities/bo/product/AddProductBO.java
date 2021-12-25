package com.ec.commons.entities.bo.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddProductBO {
    private Long sno;
    private String name;
    private Integer stockTotal;
    private Integer stockUsed;
    private Integer stockResidue;
    private Date createTime;
    private Date updateTime;
}