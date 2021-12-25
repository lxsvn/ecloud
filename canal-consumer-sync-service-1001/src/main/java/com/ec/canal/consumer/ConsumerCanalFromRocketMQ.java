package com.ec.canal.consumer;

import com.alibaba.fastjson.JSON;
import com.ec.canal.model.base.BaseModelInfo;
import com.ec.commons.entities.po.product.ProductPO;
import com.ec.commons.middleware.redis.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Component
/**
 * consumerGroup:告诉mq对consumer进行分组。避免重复消费。
 * 统一个consumerGroup标识的消费端，只会收到一次。
 * 假设消费端集群，对于consumerGroup一样的服务，同意的数据只会周收到一次
 *
 * topic:此处的topic配置在canal后台的instance.propertios配置的canal.mq.topic中
 * */
@RocketMQMessageListener(
        topic = "canal_sync_t_product",
        selectorExpression = "*",
        consumerGroup = "1001x1")
public class ConsumerCanalFromRocketMQ implements RocketMQListener<MessageExt> {

    @Resource
    private RedisUtil redisTemplate;

    @Override
    public void onMessage(MessageExt messageExt) {
        byte[] body = messageExt.getBody();
        String msg = new String(body);
        BaseModelInfo baseInfo =
                JSON.parseObject(msg, BaseModelInfo.class);
        //log.info("revive msg:" + msg);
        //当前topic只监听seata_accout.t_account
        //所以不用再判断数据库和表
        //根据不同的表区分解析

        //反序列化
        List<ProductPO> list =
                JSON.parseArray(baseInfo.getData(), ProductPO.class);

        //更新redis
//        list.forEach(item -> {
//            redisTemplate.set("user_" + item.getUserId(),
//                    JSON.toJSONString(item));
//        });


    }

}
