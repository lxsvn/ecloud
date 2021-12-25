/*
 Navicat Premium Data Transfer

 Source Server         : 47.104.247.85(qd-master-3306)
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : 47.104.247.85:3306
 Source Schema         : seata_order

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 23/12/2021 19:04:13
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_order_1
-- ----------------------------
DROP TABLE IF EXISTS `t_order_1`;
CREATE TABLE `t_order_1`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `order_no` bigint(0) NOT NULL,
  `native_id` varbinary(16) NOT NULL,
  `status` int(0) NOT NULL,
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 29 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
