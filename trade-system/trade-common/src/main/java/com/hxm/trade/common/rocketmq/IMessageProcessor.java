package com.hxm.trade.common.rocketmq;

import org.apache.rocketmq.common.message.MessageExt;

public interface IMessageProcessor {
    public boolean handleMessage(MessageExt messageExt);
}
