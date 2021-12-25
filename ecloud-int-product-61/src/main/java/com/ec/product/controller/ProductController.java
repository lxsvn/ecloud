package com.ec.product.controller;


import cn.hutool.core.lang.Assert;
import com.ec.commons.controller.BaseController;
import com.ec.commons.entities.bo.product.ProductBO;
import com.ec.commons.entities.dto.product.AddProductDTO;
import com.ec.commons.entities.dto.product.DeductStockDTO;
import com.ec.commons.entities.vo.product.ProductVO;
import com.ec.commons.util.BeanUtils;
import com.ec.commons.util.ret.R;
import com.ec.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController extends BaseController {

    @Resource
    private ProductService productService;

    @GetMapping("/info/{sno}")
    public R info(@PathVariable String sno) {
        ProductBO info = productService.getProductInfoBySno(sno);

        Assert.notNull(info, "不存在或已下架！");

        ProductVO vo = BeanUtils.toBean(info, ProductVO.class);

        return R.success("获取成功！", vo);
    }

    @PostMapping("/add")
    public R add(@RequestBody AddProductDTO dto) {
        productService.add(dto);
        return R.success("新增成功！");
    }

    @PostMapping("/deductStock")
    public R deductStock(@RequestBody DeductStockDTO dto) {
        productService.deductStock(dto.getSno(), dto.getQuantity());
        return R.success("库存扣减成功！");
    }
}