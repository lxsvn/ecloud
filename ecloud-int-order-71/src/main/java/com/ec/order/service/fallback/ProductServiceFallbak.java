package com.ec.order.service.fallback;

import com.ec.commons.entities.dto.product.DeductStockDTO;
import com.ec.commons.util.ret.R;
import com.ec.order.service.ProductService;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceFallbak implements ProductService {

    @Override
    public R deductStock(DeductStockDTO dto) {
        return null;
    }
}