package com.ec.initialize.config;

import com.ec.commons.util.StringUtilExtend;
import com.ec.commons.util.UriUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;

@Component
@Slf4j
public class DataSourceInitial implements ApplicationRunner {
    @Value("${spring.initials.enable}")
    private boolean enable;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        init();
    }

    /**
     * 初始化数据库
     * 数据库：创建数据库
     */
    public void init() throws ClassNotFoundException, URISyntaxException, SQLException {

        if (enable) {
            DataSourceConfig.dataSourceMap.forEach((name, value) -> {
                try {
                    LinkedHashMap item = ((LinkedHashMap) value);
                    String driver = StringUtilExtend.Trim(item.get("driver-class-name"));
                    String url = StringUtilExtend.Trim(item.get("url"));
                    String userName = StringUtilExtend.Trim(item.get("username"));
                    String password = StringUtilExtend.Trim(item.get("password"));
                    String schema = StringUtilExtend.Trim(item.get("schema"));

                    Class.forName(driver);
                    URI uri = new URI(url.replace("jdbc:", ""));
                    String host = uri.getHost();
                    int port = uri.getPort();
                    String path = uri.getPath();

                    //mysql版本获取
                    String pa = StringUtilExtend.Trim(UriUtil.getPara(uri.getQuery(), "useSSL"));
                    String ssl = "?useSSL=" + pa.toLowerCase();

                    // 1. 数据库初始化
                    try {
                        String connectUrl = "jdbc:mysql://" + host + ":" + port + ssl;
                        try (Connection connection =
                                     DriverManager.getConnection(connectUrl, userName, password);
                             Statement statement = connection.createStatement()) {

                            // 创建数据库
                            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS `" + path.replace("/", "") + "` DEFAULT CHARACTER SET = `utf8` COLLATE `utf8_general_ci`;");
                            log.info("数据库：" + path + " 创建成功！");
                        }

                    } catch (Exception exception) {
                        log.info("数据库初始化失败：" + exception);
                    }

                    // 2. 表初始化
                    try {
                        String connectUrl = url;
                        try (Connection connection =
                                     DriverManager.getConnection(connectUrl, userName, password);
                             Statement statement = connection.createStatement()) {

                            // 创建表
                            if (!StringUtilExtend.IsEmpty(schema)) {
                                ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
                                Resource[] sqlFiles = resourceResolver.getResources(schema);
                                for (Resource sqlFile : sqlFiles) {
                                    ScriptUtils.executeSqlScript(connection, sqlFile);
                                    log.info("数据库：" + path + " 执行：" + sqlFile + " 成功！");
                                }
                            }

                        }

                    } catch (Exception exception) {
                        log.info("表初始化失败：" + exception);
                    }
                } catch (Exception ex) {

                }
            });


        } else {
            log.error("初始化未启用!请在yml中配置：spring.initials.enable!");
        }
    }

}
