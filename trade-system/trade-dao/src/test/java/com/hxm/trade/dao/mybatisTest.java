package com.hxm.trade.dao;

import com.hxm.trade.dao.mapper.TradeUserMapper;
import com.hxm.trade.dao.pojo.TradeUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring/spring-dao.xml")
public class mybatisTest {

    @Autowired
    TradeUserMapper tradeUserMapper;

    @Test
    public void test(){
        TradeUser tradeUser=new TradeUser();
        tradeUser.setUserName("青青");
        tradeUser.setUserPassword("123321");
        int i=tradeUserMapper.insert(tradeUser);
        System.out.println(i);

    }
}
