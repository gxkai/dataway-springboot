package com.dataway.cn.activeMq;

import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Queue;
import javax.jms.Topic;

/**
 * ActiveMq 生产者
 * @author phil
 * @date 2020/07/28 16:03
 */
@RestController
public class ActiveMqProducer {

    private final JmsMessagingTemplate jmsTemplate;

    private final Queue queue;

    private final Topic topic;

    public ActiveMqProducer(JmsMessagingTemplate jmsTemplate,Queue queue,Topic topic){
        this.jmsTemplate=jmsTemplate;
        this.queue=queue;
        this.topic=topic;
    }

    /**
     * 发送queue类型消息
     * @param msg：
     */
    @GetMapping("/queue")
    public void sendQueueMsg(String msg){
        jmsTemplate.convertAndSend(queue, msg);
    }

    /**
     * 发送topic类型消息
     * @param msg：
     */
    @GetMapping("/topic")
    public void sendTopicMsg(String msg){
        jmsTemplate.convertAndSend(topic, msg);
    }
}
