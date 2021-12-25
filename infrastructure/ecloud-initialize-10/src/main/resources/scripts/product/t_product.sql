/*
 Navicat Premium Data Transfer

 Source Server         : 47.104.247.85(qd-master-3306)
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : 47.104.247.85:3306
 Source Schema         : ecloud_product

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 24/12/2021 14:04:52
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_product
-- ----------------------------
DROP TABLE IF EXISTS `t_product`;
CREATE TABLE `t_product`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT,
  `sno` bigint(0) NOT NULL COMMENT '产品sn码。唯一',
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '产品名称',
  `stock_total` int(0) NOT NULL COMMENT '总库存',
  `stock_used` int(0) NOT NULL COMMENT '已用库存',
  `stock_residue` int(0) NOT NULL COMMENT '剩余库存',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '最后修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
