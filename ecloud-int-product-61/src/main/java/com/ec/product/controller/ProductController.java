package com.ec.product.controller;


import cn.hutool.core.lang.Assert;
import cn.hutool.json.JSONUtil;
import com.ec.apis.product.IProductService;
import com.ec.commons.constant.RedisKeyConstant;
import com.ec.commons.controller.BaseController;
import com.ec.commons.entities.bo.product.ProductBO;
import com.ec.commons.entities.dto.product.AddProductDTO;
import com.ec.commons.entities.dto.product.DeductStockDTO;
import com.ec.commons.entities.vo.product.ProductVO;
import com.ec.commons.util.BeanUtils;
import com.ec.commons.util.StringUtilExtend;
import com.ec.commons.util.ret.R;
import com.edward.redis.template.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController extends BaseController {

    @Resource
    private IProductService productService;

    @Autowired
    private RedisUtil redisTemplate;

    @GetMapping("/info/{sno}")
    public R info(@PathVariable String sno) {
        Object info = productService.getProductInfoBySno(sno);

        Assert.notNull(info, "不存在或已下架！");

        ProductVO vo = BeanUtils.toBean(info, ProductVO.class);

        return R.success("获取成功！", vo);
    }

    @GetMapping("/info/cache/{sno}")
    public R infoCache(@PathVariable String sno) {

        String str = StringUtilExtend.Trim(redisTemplate.get(RedisKeyConstant.PRODUCT_INFO + sno));

        Assert.isFalse("".equals(str), "不存在或已下架！");

        ProductVO vo = JSONUtil.toBean(str, ProductVO.class);

        return R.success("获取成功！", vo);
    }

    @PostMapping("/add")
    public R add(@RequestBody AddProductDTO dto) {
        productService.add(dto);
        return R.success("新增成功！");
    }

    @PostMapping("/deductStock")
    public R deductStock(@RequestBody DeductStockDTO dto) {
        productService.deductStock(dto);
        return R.success("库存扣减成功！");
    }
}