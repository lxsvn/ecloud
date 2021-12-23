package com.ec.commons.entities.po.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountPO implements Serializable {

    /**
     * 用户唯一标识。全局流通标识
     * ps：分表的情况下，自增ID不满足唯一要求
     * */
    private String nativeId;
    /**
     * 主键ID
     * */
    private Long id;

    /**
     * 总额度
     */
    private Integer total;

    /**
     * 已用额度
     */
    private Integer used;

    /**
     * 剩余额度
     */
    private Integer residue;

    private String mobile;
    private String name;
    private String pwd;
}
