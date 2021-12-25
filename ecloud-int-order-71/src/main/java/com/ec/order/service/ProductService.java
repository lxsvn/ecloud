package com.ec.order.service;

import com.ec.commons.entities.dto.product.DeductStockDTO;
import com.ec.commons.util.ret.R;
import com.ec.order.service.fallback.AccountServiceFallbak;
import com.ec.order.service.fallback.ProductServiceFallbak;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "ecloud-int-product-service",
        fallback = ProductServiceFallbak.class)
public interface ProductService {

    @PostMapping(value = "/product/deductStock",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    R deductStock(@RequestBody DeductStockDTO dto);
}
