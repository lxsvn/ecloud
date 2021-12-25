package com.ec.product.dao;

import com.ec.commons.entities.bo.product.AddProductBO;
import com.ec.commons.entities.po.product.ProductPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface ProductDao {

    void add(AddProductBO bo);

    ProductPO getProductInfoBySno(String sno);

    void deductStock(@Param("sno") String sno,
                     @Param("quantity") int quantity);
}
