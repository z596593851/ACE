package com.hxm.trade.common.client;

import com.hxm.trade.common.protocol.user.QueryUserReq;
import com.hxm.trade.common.protocol.user.QueryUserRes;
import org.springframework.web.client.RestTemplate;

public class RestClient {
    private static RestTemplate restTemplate=new RestTemplate();
    public static void main(String[] args){
        QueryUserReq queryUserReq=new QueryUserReq();
        queryUserReq.setUserId(1);
        QueryUserRes queryUserRes=restTemplate.postForObject("http://localhost:8090/user/queryUserById",queryUserReq,QueryUserRes.class);
        System.out.println(queryUserRes);
    }
}
