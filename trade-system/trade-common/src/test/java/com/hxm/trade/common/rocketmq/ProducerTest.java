package com.hxm.trade.common.rocketmq;

import com.hxm.trade.common.api.IUserApi;
import com.hxm.trade.common.exception.AceMQException;
import com.hxm.trade.common.protocol.user.QueryUserReq;
import com.hxm.trade.common.protocol.user.QueryUserRes;
import com.hxm.trade.common.rocketmq.AceMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring.xml")
public class ProducerTest {
    @Autowired
    private AceMQProducer aceMQProducer;

    @Autowired
    private IUserApi userApi;

    @Test
    public void testProducer() throws AceMQException{

            SendResult sendResult=aceMQProducer.sendMessage("Test2Topic","order","12345678","this is order message");
            System.out.println(sendResult);
    }

    @Test
    public void testConsumer() throws Exception{
        Thread.sleep(1000000L);
    }

    @Test
    public void clientTest(){
        QueryUserReq queryUserReq=new QueryUserReq();
        queryUserReq.setUserId(1);
        QueryUserRes queryUserRes=userApi.queryUserById(queryUserReq);
        System.out.println(queryUserRes);

    }
}
