package com.ec.product.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.lang.Snowflake;
import com.ec.commons.entities.bo.account.AccountBO;
import com.ec.commons.entities.bo.account.RegisterBO;
import com.ec.commons.entities.bo.product.AddProductBO;
import com.ec.commons.entities.bo.product.ProductBO;
import com.ec.commons.entities.dto.product.AddProductDTO;
import com.ec.commons.entities.po.product.ProductPO;
import com.ec.commons.util.BeanUtils;
import com.ec.commons.util.SnowflakeUtil;
import com.ec.commons.util.UUIDUtil;
import com.ec.product.dao.ProductDao;
import com.ec.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class ProductServiceImpl implements ProductService {
    @Resource
    private ProductDao productDao;

    @Override
    public void add(AddProductDTO dto) {

        AddProductBO bo = BeanUtils.toBean(dto, AddProductBO.class);
        //生成商品唯一编号：雪花ID
        Long snNo = SnowflakeUtil.getSnowflakeId();
        bo.setSno(snNo);
        bo.setCreateTime(DateTime.now());
        bo.setUpdateTime(DateTime.now());


        productDao.add(bo);

    }

    @Override
    public ProductBO getProductInfoBySno(String sno) {
        ProductPO info = productDao.getProductInfoBySno(sno);

        ProductBO bo = BeanUtils.toBean(info, ProductBO.class);
        return bo;
    }

    @Override
    public void deductStock(String sno, int quantity) {
        productDao.deductStock(sno, quantity);
    }
}