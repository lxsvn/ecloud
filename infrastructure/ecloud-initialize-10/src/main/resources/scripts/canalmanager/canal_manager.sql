SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for canal_adapter_config
-- ----------------------------
DROP TABLE IF EXISTS `canal_adapter_config`;
CREATE TABLE `canal_adapter_config` (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                        `category` varchar(45) NOT NULL,
                                        `name` varchar(45) NOT NULL,
                                        `status` varchar(45) DEFAULT NULL,
                                        `content` text NOT NULL,
                                        `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for canal_cluster
-- ----------------------------
DROP TABLE IF EXISTS `canal_cluster`;
CREATE TABLE `canal_cluster` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                 `name` varchar(63) NOT NULL,
                                 `zk_hosts` varchar(255) NOT NULL,
                                 `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for canal_config
-- ----------------------------
DROP TABLE IF EXISTS `canal_config`;
CREATE TABLE `canal_config` (
                                `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                `cluster_id` bigint(20) DEFAULT NULL,
                                `server_id` bigint(20) DEFAULT NULL,
                                `name` varchar(45) NOT NULL,
                                `status` varchar(45) DEFAULT NULL,
                                `content` text NOT NULL,
                                `content_md5` varchar(128) NOT NULL,
                                `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `sid_UNIQUE` (`server_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for canal_instance_config
-- ----------------------------
DROP TABLE IF EXISTS `canal_instance_config`;
CREATE TABLE `canal_instance_config` (
                                         `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                         `cluster_id` bigint(20) DEFAULT NULL,
                                         `server_id` bigint(20) DEFAULT NULL,
                                         `name` varchar(45) NOT NULL,
                                         `status` varchar(45) DEFAULT NULL,
                                         `content` text NOT NULL,
                                         `content_md5` varchar(128) DEFAULT NULL,
                                         `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                         PRIMARY KEY (`id`),
                                         UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for canal_node_server
-- ----------------------------
DROP TABLE IF EXISTS `canal_node_server`;
CREATE TABLE `canal_node_server` (
                                     `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                     `cluster_id` bigint(20) DEFAULT NULL,
                                     `name` varchar(63) NOT NULL,
                                     `ip` varchar(63) NOT NULL,
                                     `admin_port` int(11) DEFAULT NULL,
                                     `tcp_port` int(11) DEFAULT NULL,
                                     `metric_port` int(11) DEFAULT NULL,
                                     `status` varchar(45) DEFAULT NULL,
                                     `modified_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for canal_user
-- ----------------------------
DROP TABLE IF EXISTS `canal_user`;
CREATE TABLE `canal_user` (
                              `id` bigint(20) NOT NULL AUTO_INCREMENT,
                              `username` varchar(31) NOT NULL,
                              `password` varchar(128) NOT NULL,
                              `name` varchar(31) NOT NULL,
                              `roles` varchar(31) NOT NULL,
                              `introduction` varchar(255) DEFAULT NULL,
                              `avatar` varchar(255) DEFAULT NULL,
                              `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------
-- Records of canal_user  admin/123456
-- 密码加密方式：https://github.com/oceanbase/canal/wiki/%E9%83%A8%E7%BD%B2canal.deployer%EF%BC%88MySQL%E4%BD%9C%E4%B8%BA%E6%95%B0%E6%8D%AE%E6%BA%90%EF%BC%89
-- https://github.com/alibaba/canal/blob/master/protocol/src/main/java/com/alibaba/otter/canal/protocol/SecurityUtil.java#L43
-- ----------------------------
BEGIN;
INSERT INTO `canal_user` VALUES (1, 'admin', '6BB4837EB74329105EE4568DDA7DC67ED2CA2AD9', 'Canal Manager', 'admin', NULL, NULL, '2019-07-14 00:05:28');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;