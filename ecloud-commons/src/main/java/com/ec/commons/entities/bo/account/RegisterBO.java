package com.ec.commons.entities.bo.account;

import lombok.Data;

@Data
public class RegisterBO {
    private String nativeId;
    private String mobile;
    private String name;
    private String pwd;
    private double total;
}
