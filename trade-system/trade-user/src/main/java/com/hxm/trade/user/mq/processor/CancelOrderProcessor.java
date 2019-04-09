package com.hxm.trade.user.mq.processor;

import com.alibaba.fastjson.JSON;
import com.hxm.trade.common.constants.TradeEnums;
import com.hxm.trade.common.protocol.mq.CancelOrderMQ;
import com.hxm.trade.common.protocol.user.ChangeUserMoneyReq;
import com.hxm.trade.common.rocketmq.IMessageProcessor;
import com.hxm.trade.user.service.IUserService;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
public class CancelOrderProcessor implements IMessageProcessor {
    @Autowired
    private IUserService userService;
    public static final Logger LOGGER= LoggerFactory.getLogger(CancelOrderProcessor.class);
    public boolean handleMessage(MessageExt messageExt) {
        try {
            String body=new String(messageExt.getBody(),"UTF-8");
            String msgId=messageExt.getMsgId();
            String tags=messageExt.getTags();
            String keys=messageExt.getKeys();
            LOGGER.info("user CancelOrderProcessor recive message:"+body);
            CancelOrderMQ cancelOrderMQ= JSON.parseObject(body,CancelOrderMQ.class);
            //退款
            if(cancelOrderMQ.getUserMoney()!=null&&cancelOrderMQ.getUserMoney().compareTo(BigDecimal.ZERO)==1){
                ChangeUserMoneyReq changeUserMoneyReq=new ChangeUserMoneyReq();
                changeUserMoneyReq.setUserId(cancelOrderMQ.getUserId());
                changeUserMoneyReq.setMoneyLogType(TradeEnums.UserMoneyLogTypeEnum.REFUND.getCode());
                changeUserMoneyReq.setOrderId(cancelOrderMQ.getOrderId());
                userService.changeUserMoney(changeUserMoneyReq);
            }
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
