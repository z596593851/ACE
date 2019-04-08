package com.hxm.trade.common.api;

import com.hxm.trade.common.protocol.user.QueryUserReq;
import com.hxm.trade.common.protocol.user.QueryUserRes;

public interface IUserApi {
    public QueryUserRes queryUserById(QueryUserReq queryUserReq);
}
