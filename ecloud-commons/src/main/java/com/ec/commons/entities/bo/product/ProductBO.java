package com.ec.commons.entities.bo.product;

import com.ec.commons.entities.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductBO  extends BaseEntity {
    private Long id;
    private Long sno;
    private String name;
    private Integer stockTotal;
    private Integer stockUsed;
    private Integer stockResidue;
    private Date createTime;
    private Date updateTime;
}