package com.hxm.trade.user.service.impl;

import com.hxm.trade.common.constants.TradeEnums;
import com.hxm.trade.common.protocol.user.ChangeUserMoneyReq;
import com.hxm.trade.common.protocol.user.ChangeUserMoneyRes;
import com.hxm.trade.common.protocol.user.QueryUserReq;
import com.hxm.trade.common.protocol.user.QueryUserRes;
import com.hxm.trade.dao.mapper.TradeUserMapper;
import com.hxm.trade.dao.mapper.TradeUserMoneyLogMapper;
import com.hxm.trade.dao.pojo.TradeUser;
import com.hxm.trade.dao.pojo.TradeUserMoneyLog;
import com.hxm.trade.dao.pojo.TradeUserMoneyLogExample;
import com.hxm.trade.user.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.Date;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private TradeUserMapper tradeUserMapper;
    @Autowired
    private TradeUserMoneyLogMapper tradeUserMoneyLogMapper;

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
            queryUserRes.setRetInfo(e.getMessage());

        }
        return queryUserRes;
    }

    @Transactional
    public ChangeUserMoneyRes changeUserMoney(ChangeUserMoneyReq changeUserMoneyReq) {
        ChangeUserMoneyRes changeUserMoneyRes=new ChangeUserMoneyRes();
        changeUserMoneyRes.setRetCode(TradeEnums.RetEnum.SUCCESS.getCode());
        changeUserMoneyRes.setRetInfo(TradeEnums.RetEnum.SUCCESS.getDesc());

        try{
            if(changeUserMoneyReq==null||changeUserMoneyReq.getUserId()==null||changeUserMoneyReq.getUserMoney()==null){
                throw new RuntimeException("请求参数不正确");
            }
            if(changeUserMoneyReq.getUserMoney().compareTo(BigDecimal.ZERO)<=0){
                throw new RuntimeException("金额不能小于0");
            }
            TradeUserMoneyLog tradeUserMoneyLog=new TradeUserMoneyLog();
            tradeUserMoneyLog.setOrderId(changeUserMoneyReq.getOrderId());
            tradeUserMoneyLog.setUserId(changeUserMoneyReq.getUserId());
            tradeUserMoneyLog.setUserMoney(changeUserMoneyReq.getUserMoney());
            tradeUserMoneyLog.setCreateTime(new Date());
            tradeUserMoneyLog.setMoneyLogType(changeUserMoneyReq.getMoneyLogType());
            TradeUser tradeUser=new TradeUser();
            tradeUser.setUserId(changeUserMoneyReq.getUserId());
            tradeUser.setUserMoney(changeUserMoneyReq.getUserMoney());

            //查询是否有付款记录
            TradeUserMoneyLogExample logExample=new TradeUserMoneyLogExample();
            logExample.createCriteria()
                    .andUserIdEqualTo(changeUserMoneyReq.getUserId())
                    .andOrderIdEqualTo(changeUserMoneyReq.getOrderId())
                    .andMoneyLogTypeEqualTo(TradeEnums.UserMoneyLogTypeEnum.PAID.getCode());
            int count=tradeUserMoneyLogMapper.countByExample(logExample);
            //订单付款
            if(changeUserMoneyReq.getMoneyLogType().equals(TradeEnums.UserMoneyLogTypeEnum.PAID.getCode())){
                if(count>0){
                    throw new RuntimeException("已经付过款，不能重复付款");
                }
                tradeUserMapper.reduceUserMoney(tradeUser);
            }
            //订单退款
            if (changeUserMoneyReq.getMoneyLogType().equals(TradeEnums.UserMoneyLogTypeEnum.REFUND.getCode())){

                if(count==0){
                    throw new RuntimeException("没有付款信息，不能退款");
                }
                //防止多次退款
                logExample=new TradeUserMoneyLogExample();
                logExample.createCriteria()
                        .andUserIdEqualTo(changeUserMoneyReq.getUserId())
                        .andOrderIdEqualTo(changeUserMoneyReq.getOrderId())
                        .andMoneyLogTypeEqualTo(TradeEnums.UserMoneyLogTypeEnum.REFUND.getCode());
                count=tradeUserMoneyLogMapper.countByExample(logExample);
                if(count>0){
                    throw new RuntimeException("已经退过款了，不能退款");
                }
                tradeUserMapper.addUserMoney(tradeUser);
            }
            tradeUserMoneyLogMapper.insert(tradeUserMoneyLog);
        }catch (Exception e){
            changeUserMoneyRes.setRetCode(TradeEnums.RetEnum.FAIL.getCode());
            changeUserMoneyRes.setRetInfo(e.getMessage());
        }


        return changeUserMoneyRes;
    }
}
