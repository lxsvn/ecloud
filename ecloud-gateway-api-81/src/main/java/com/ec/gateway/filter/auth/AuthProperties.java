package com.ec.gateway.filter.auth;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 验证权限配置
 *
 * @author pangu
 * @date 2020-10-28
 */
@Data
@RefreshScope
@Component
@ConfigurationProperties(prefix = "auth")

public class AuthProperties {

    /**
     * 忽略URL，List列表形式
     */
    private List<String> ignoreUrl = new ArrayList<>();

    /**
     * 是否启用网关鉴权模式
     */
    private Boolean enable = false;

    /**
     * 初始化一些无需授权访问的接口
     */
    private static final String[] ENDPOINTS = {
            "/oauth/**",
            "/actuator/**",
            "/v2/api-docs/**",
            "/v2/api-docs-ext/**",
            "/swagger/api-docs",
            "/swagger-ui.html",
            "/doc.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/druid/**",
            "/error/**",
            "/assets/**",
            "/auth/logout",
            "/auth/code"
    };

    /**
     * 自定义getter方法，并将ENDPOINTS加入至忽略URL列表
     * @return List
     */
    public List<String> getIgnoreUrl() {
        if (!ignoreUrl.contains("/doc.html")) {
            Collections.addAll(ignoreUrl, ENDPOINTS);
        }
        return ignoreUrl;
    }

    public Boolean getEnable() {
        return enable;
    }
}
