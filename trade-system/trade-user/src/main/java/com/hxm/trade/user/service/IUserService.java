package com.hxm.trade.user.service;

import com.hxm.trade.common.protocol.user.QueryUserReq;
import com.hxm.trade.common.protocol.user.QueryUserRes;

public interface IUserService {
    public QueryUserRes queryUserById(QueryUserReq queryUserReq);
}
