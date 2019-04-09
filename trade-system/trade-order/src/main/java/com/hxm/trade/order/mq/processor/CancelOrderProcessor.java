package com.hxm.trade.order.mq.processor;

import com.alibaba.fastjson.JSON;
import com.hxm.trade.common.constants.TradeEnums;
import com.hxm.trade.common.protocol.mq.CancelOrderMQ;
import com.hxm.trade.common.protocol.user.ChangeUserMoneyReq;
import com.hxm.trade.common.rocketmq.IMessageProcessor;
import com.hxm.trade.dao.mapper.TradeOrderMapper;
import com.hxm.trade.dao.pojo.TradeOrder;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

public class CancelOrderProcessor implements IMessageProcessor {
    @Autowired
    private TradeOrderMapper tradeOrderMapper;
    public static final Logger LOGGER= LoggerFactory.getLogger(CancelOrderProcessor.class);
    public boolean handleMessage(MessageExt messageExt) {
        try {
            String body=new String(messageExt.getBody(),"UTF-8");
            String msgId=messageExt.getMsgId();
            String tags=messageExt.getTags();
            String keys=messageExt.getKeys();
            LOGGER.info("user CancelOrderProcessor recive message:"+body);
            CancelOrderMQ cancelOrderMQ= JSON.parseObject(body,CancelOrderMQ.class);

            //修改订单状态为取消
            TradeOrder tradeOrder=new TradeOrder();
            tradeOrder.setOrderId(cancelOrderMQ.getOrderId());
            tradeOrder.setOrderStatus(TradeEnums.OrderStatusEnum.CANCEL.getStatusCode());
            //updateByPrimaryKey更新全部字段
            //updateByPrimaryKeySelective更新非null字段
            tradeOrderMapper.updateByPrimaryKeySelective(tradeOrder);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
