package com.hxm.trade.common.api;

import com.hxm.trade.common.protocol.user.QueryUserReq;
import com.hxm.trade.common.protocol.user.QueryUserRes;
import org.springframework.stereotype.Component;

@Component
public interface IUserApi {
    public QueryUserRes queryUserById(QueryUserReq queryUserReq);
}
