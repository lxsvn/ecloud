package com.ec.commons.filter.log;

import lombok.Data;

/**
 *
 */
@Data
public class LogInfo {
    private String type;
    private String method;
    private String msg;
    private String url;
    private String ip;
    private String os;
    private String queryParams;
    private String body;
    private String headers;

}
