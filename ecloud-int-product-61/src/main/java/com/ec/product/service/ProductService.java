package com.ec.product.service;

import com.ec.commons.entities.bo.product.ProductBO;
import com.ec.commons.entities.dto.product.AddProductDTO;
import org.springframework.stereotype.Service;


@Service
public interface ProductService {
    /**
     * 新增
     */
    void add(AddProductDTO dto);

    /**
     * 获取 by sno
     */
    ProductBO getProductInfoBySno(String sno);

    /**
     * 扣减库存
     */
    void deductStock(String sno,int quantity);

}