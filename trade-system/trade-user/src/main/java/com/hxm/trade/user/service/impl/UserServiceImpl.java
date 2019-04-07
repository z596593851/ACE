package com.hxm.trade.user.service.impl;

import com.hxm.trade.common.constants.TradeEnums;
import com.hxm.trade.common.protocol.user.QueryUserReq;
import com.hxm.trade.common.protocol.user.QueryUserRes;
import com.hxm.trade.dao.mapper.TradeUserMapper;
import com.hxm.trade.dao.pojo.TradeUser;
import com.hxm.trade.user.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private TradeUserMapper tradeUserMapper;
    public QueryUserRes queryUserById(QueryUserReq queryUserReq) {
        QueryUserRes queryUserRes=new QueryUserRes();
        queryUserRes.setRetCode(TradeEnums.RetEnum.SUCCESS.getCode());
        queryUserRes.setRetInfo(TradeEnums.RetEnum.SUCCESS.getDesc());
        try{
            if(queryUserReq==null||queryUserReq.getUserId()==null)
                throw new RuntimeException("请求参数不正确");
            TradeUser tradeUser=tradeUserMapper.selectByPrimaryKey(queryUserReq.getUserId());
            if(tradeUser!=null)
                BeanUtils.copyProperties(tradeUser,queryUserRes);
            else
                throw new RuntimeException("未查询到该用户");
        }catch (Exception e){
            queryUserRes.setRetCode(TradeEnums.RetEnum.FAIL.getCode());
            queryUserRes.setRetInfo(TradeEnums.RetEnum.FAIL.getDesc());

        }
        return queryUserRes;
    }
}
