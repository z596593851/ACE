package com.hxm.trade.common.rocketmq;

import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

@Component
public class TestProcessor implements IMessageProcessor {
    public boolean handleMessage(MessageExt messageExt) {
        System.out.println("收到消息："+messageExt.toString());
        return true;
    }
}
