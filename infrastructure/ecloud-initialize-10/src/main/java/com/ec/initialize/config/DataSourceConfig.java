package com.ec.initialize.config;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import com.ec.commons.util.PropertyUtil;
import com.ec.commons.util.StringUtilExtend;
import com.ec.commons.util.UriUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.*;

@Configuration
@Slf4j
public class DataSourceConfig implements EnvironmentAware {

    public static final Map<String, Object> dataSourceMap = new LinkedHashMap<>();

    @Override
    public void setEnvironment(Environment environment) {
        String prefix = "spring.initials.datasource.";
        String[] names = environment.getProperty(prefix + "names").split(",");
        for (String name : names) {
            try {
                Map<String, Object> map = PropertyUtil.handle(environment, prefix + name.trim(), Map.class);
                dataSourceMap.put(name, map);
            } catch (Exception ex) {

            }
        }
    }

}