package com.ec.gateway.schedules;

import com.ec.commons.util.StringUtilExtend;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 定时推送日志任务 -> kafka
 * 通过ELK分析日志
 */

@Component
@Slf4j
public class LogPushSchedule {

    private final String TOPIC_NAME = "ecloud_api_gateway_log";
    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;

    /**
     * 日志队列
     */
    public static ConcurrentLinkedQueue logQueue = new ConcurrentLinkedQueue();


    /**
     * 推送任务
     */
    @Scheduled(fixedRate = 2000)
    public void logPushToKafka() {
        if (!logQueue.isEmpty()) {
            String val = StringUtilExtend.Trim(logQueue.poll());
            kafkaTemplate.send(TOPIC_NAME, val);
        }
    }
}
