package com.hxm.trade.user.service;

import com.hxm.trade.common.protocol.user.ChangeUserMoneyReq;
import com.hxm.trade.common.protocol.user.ChangeUserMoneyRes;
import com.hxm.trade.common.protocol.user.QueryUserReq;
import com.hxm.trade.common.protocol.user.QueryUserRes;

public interface IUserService {
    public QueryUserRes queryUserById(QueryUserReq queryUserReq);
    public ChangeUserMoneyRes changeUserMoney(ChangeUserMoneyReq changeUserMoneyReq);
}
