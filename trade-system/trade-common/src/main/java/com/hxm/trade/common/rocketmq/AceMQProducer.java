package com.hxm.trade.common.rocketmq;

import com.hxm.trade.common.exception.AceMQException;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Component
@PropertySource(value = {"classpath:spring-value.properties"})
public class AceMQProducer {
    public static final Logger LOGGER= LoggerFactory.getLogger(AceMQProducer.class);
    private DefaultMQProducer producer;
    @Value("${groupName}")
    private String groupName;
    @Value("${namesrvAddr}")
    private String namesrvAddr;
    private int maxMessageSize=1024*1024*4;
    private int sendMsgTimeout=1000;

    @PostConstruct
    public void init() throws AceMQException {
        if(StringUtils.isBlank(this.groupName)){
            throw new AceMQException("groupName is blank");
        }
        if(StringUtils.isBlank(this.namesrvAddr)){
            throw new AceMQException("namesrvAddr is blank");
        }
        producer=new DefaultMQProducer(groupName);
        producer.setNamesrvAddr(namesrvAddr);
        producer.setMaxMessageSize(maxMessageSize);
        producer.setSendMsgTimeout(sendMsgTimeout);
        try {
            producer.start();
            LOGGER.info(String.format("producer is start!groupName:[%s],namesrvAddr:[%s]",this.groupName,this.namesrvAddr));
        }catch (MQClientException e){
            LOGGER.error(String.format("producer error!groupName:[%s],namesrvAddr:[%s]",this.groupName,this.namesrvAddr));
            throw new AceMQException(e);
        }
    }

    public SendResult sendMessage(String topic,String tags,String keys,String messageText) throws AceMQException{
        if(StringUtils.isBlank(topic)){
            throw new AceMQException("topic is blank");
        }
        if(StringUtils.isBlank(messageText)){
            throw new AceMQException("messageText is blank");
        }
        Message  message=new Message(topic,tags,keys,messageText.getBytes());
        try {
            SendResult sendResult=producer.send(message);
            return sendResult;
        }catch (Exception e){
            LOGGER.error("send message error:",e.getMessage());
            throw new AceMQException(e);
        }

    }
}
