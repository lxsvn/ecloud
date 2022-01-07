package com.ec.apis.product;

import com.ec.commons.entities.bo.product.ProductBO;
import com.ec.commons.entities.dto.product.AddProductDTO;
import com.ec.commons.entities.dto.product.DeductStockDTO;

/**
 * 产品接口
 *
 * */
public interface IProductService {
    /**
     * 新增
     */
    void add(AddProductDTO dto);

    /**
     * 获取 by sno
     */
    Object getProductInfoBySno(String sno);

    /**
     * 扣减库存
     */
    void deductStock(DeductStockDTO dto);
}
