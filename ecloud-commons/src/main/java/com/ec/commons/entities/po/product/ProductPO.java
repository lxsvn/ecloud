package com.ec.commons.entities.po.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPO {
    private Long id;
    private Long sno;
    private String name;
    private Integer stockTotal;
    private Integer stockUsed;
    private Integer stockResidue;
    private Date createTime;
    private Date updateTime;
}