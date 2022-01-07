//package com.ec.order.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
//import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
//import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
//import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;
//
//import java.util.Collection;
//
//@Slf4j
///**
// *
// * @自定义分片算法
// * 找出当前数据需要落入哪张表
// * PreciseShardingAlgorithm：精确分片算法类名称，用于=和IN。。该类需实现PreciseShardingAlgorithm接口并提供无参数的构造器
// * RangeShardingAlgorithm：范围分片算法类名称，用于BETWEEN，可选。。该类需实现RangeShardingAlgorithm接口并提供无参数的构造器
// *
// *
// * 按需选择
// * */
//public class CustomShardingAlgorithmOrderItemConfig implements PreciseShardingAlgorithm, RangeShardingAlgorithm {
//
//
//    /**
//     * 精确分片算法
//     * in/= 判断标准
//     *
//     * @param collection
//     * @param preciseShardingValue
//     * @return 数据需要落入的目标表名
//     */
//    @Override
//    public String doSharding(Collection collection, PreciseShardingValue preciseShardingValue) {
//        // 取值。因为当前yml配置根据order_no分片
//        String orderNo = preciseShardingValue.getValue().toString();
//        // 当前操作的主表
//        String logicTableName = preciseShardingValue.getLogicTableName();
//
//        //后台三张表：t_order_item_0...t_order_item_3，所以对3取余，计算落入表。
//        //同时也取决于yml配置：actual-data-nodes
//        String index = String.valueOf(Long.parseLong(orderNo) % 3);
//        //拼接目标表名
//        String tableReal = logicTableName.concat("_").concat(index);
//        log.info("数据落入表：" + tableReal);
//        return tableReal;
//
//    }
//
//    @Override
//    public Collection<String> doSharding(Collection collection, RangeShardingValue rangeShardingValue) {
//        return null;
//    }
//
//}