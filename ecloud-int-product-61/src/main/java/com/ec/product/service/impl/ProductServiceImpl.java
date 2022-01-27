package com.ec.product.service.impl;

import cn.hutool.core.date.DateTime;
import com.ec.apis.product.IProductService;
import com.ec.commons.entities.bo.product.AddProductBO;
import com.ec.commons.entities.bo.product.ProductBO;
import com.ec.commons.entities.dto.product.AddProductDTO;
import com.ec.commons.entities.dto.product.DeductStockDTO;
import com.ec.commons.entities.po.product.ProductPO;
import com.ec.commons.util.BeanUtils;
import com.ec.commons.util.SnowflakeUtil;
import com.ec.product.dao.ProductDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

@DubboService
@Slf4j
public class ProductServiceImpl implements IProductService {
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
    public Object getProductInfoBySno(String sno) {
        ProductPO info = productDao.getProductInfoBySno(sno);

        ProductBO bo = BeanUtils.toBean(info, ProductBO.class);
        return bo;
    }

    @Override
    public void deductStock(DeductStockDTO dto) {
        throw new NullPointerException();
        //productDao.deductStock(dto.getSno(), dto.getQuantity());
    }
}