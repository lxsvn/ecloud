package com.ec.canal.model.base;

import lombok.Data;


@Data
public class BaseModelInfo {
    // list
    private  String data;
    //数据库
    private String database;
    //表名
    private String table;
    //UPDATE/INSERT/DELETE
    private String type;
}
