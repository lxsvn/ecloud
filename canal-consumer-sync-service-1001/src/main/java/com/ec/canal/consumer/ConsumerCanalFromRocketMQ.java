package com.ec.canal.consumer;

import com.alibaba.fastjson.JSON;
import com.ec.canal.model.base.BaseModelInfo;
import com.ec.commons.constant.RedisKeyConstant;
import com.ec.commons.entities.po.product.ProductPO;
import com.edward.redis.template.RedisUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
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
        consumerGroup = "1001x8")
@AllArgsConstructor
public class ConsumerCanalFromRocketMQ implements RocketMQListener<MessageExt> {


    private RedisUtil redisUtil;

    @Override
    public void onMessage(MessageExt messageExt) {
        byte[] body = messageExt.getBody();
        String msg = new String(body);
        BaseModelInfo baseInfo =
                JSON.parseObject(msg, BaseModelInfo.class);

        //不用再判断数据库和表，因为canal监听时，在instance处已配置

        //反序列化
        List<ProductPO> list =
                JSON.parseArray(baseInfo.getData(), ProductPO.class);

        //更新redis
        list.forEach(item -> {
            log.info("canal push msg:" + item.getSno());

            redisUtil.set(RedisKeyConstant.PRODUCT_INFO + item.getSno(),
                    JSON.toJSONString(item));

        });


    }

}
