package com.hxm.trade.user.api;

import com.hxm.trade.common.api.IUserApi;
import com.hxm.trade.common.protocol.user.ChangeUserMoneyReq;
import com.hxm.trade.common.protocol.user.ChangeUserMoneyRes;
import com.hxm.trade.common.protocol.user.QueryUserReq;
import com.hxm.trade.common.protocol.user.QueryUserRes;
import com.hxm.trade.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserApiImpl implements IUserApi {
    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/queryUserById",method = RequestMethod.POST)
    @ResponseBody
    public QueryUserRes queryUserById(@RequestBody QueryUserReq queryUserReq) {

        return userService.queryUserById(queryUserReq);
    }

    public ChangeUserMoneyRes changeUserMoney(ChangeUserMoneyReq changeUserMoneyReq) {
        return null;
    }
}
