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

 Date: 24/12/2021 13:28:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_order_item
-- ----------------------------
DROP TABLE IF EXISTS `t_order_item`;
CREATE TABLE `t_order_item`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `order_no` bigint(0) NOT NULL,
  `sno` bigint(0) NOT NULL,
  `quantity` int(0) NOT NULL,
  `unit_price` decimal(18, 2) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
