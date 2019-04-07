package com.hxm.trade.common.rocketmq;

import com.hxm.trade.common.exception.AceMQException;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
@Component
@PropertySource(value = {"classpath:spring-value.properties"})
public class AceMQConsumer {
    public static final Logger LOGGER= LoggerFactory.getLogger(AceMQConsumer.class);
    @Autowired
    private IMessageProcessor messageProcessor;
    @Value("${groupName}")
    private String groupName;
    @Value("${topic}")
    private String topic;
    private String tag="*";
    @Value("${namesrvAddr}")
    private String namesrvAddr;
    private int consumeThreadMin=20;
    private int consumeThreadMax=64;

    @PostConstruct
    public void init() throws AceMQException{
        if(StringUtils.isBlank(groupName)){
            throw new AceMQException("groupName is blank");
        }
        if(StringUtils.isBlank(topic)){
            throw new AceMQException("topic is blank");
        }
        if(StringUtils.isBlank(namesrvAddr)){
            throw new AceMQException("namesrvAddr is blank");
        }
        DefaultMQPushConsumer consumer=new DefaultMQPushConsumer(groupName);
        consumer.setNamesrvAddr(namesrvAddr);
        try {
            consumer.subscribe(topic,tag);
            consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
            consumer.setConsumeThreadMin(consumeThreadMin);
            consumer.setConsumeThreadMax(consumeThreadMax);
            AceMessageListener aceMessageListener=new AceMessageListener();
            aceMessageListener.setMessageProcessor(messageProcessor);
            consumer.registerMessageListener(aceMessageListener);
            consumer.start();
            LOGGER.info(String.format("consumer is start!groupName:[%s],topic:[%s],namesrvAddr:[%s]",this.groupName,this.topic,this.namesrvAddr));


        }catch (MQClientException e){
            LOGGER.error(String.format("consumer is error!groupName:[%s],topic:[%s],namesrvAddr:[%s]",this.groupName,this.topic,this.namesrvAddr));

            throw new AceMQException(e);


        }
    }
}
