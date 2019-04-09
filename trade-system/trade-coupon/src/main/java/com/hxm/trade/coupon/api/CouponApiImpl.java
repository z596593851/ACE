package com.hxm.trade.coupon.api;

import com.hxm.trade.common.api.ICouponApi;
import com.hxm.trade.common.protocol.coupon.ChangeCouponStatusReq;
import com.hxm.trade.common.protocol.coupon.ChangeCouponStatusRes;
import com.hxm.trade.common.protocol.coupon.QueryCouponReq;
import com.hxm.trade.common.protocol.coupon.QueryCouponRes;
import com.hxm.trade.coupon.service.ICouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CouponApiImpl implements ICouponApi {

    @Autowired
    private ICouponService couponService;

    @RequestMapping(value = "/queryCoupon",method = RequestMethod.POST)
    @ResponseBody
    public QueryCouponRes queryCoupon(QueryCouponReq queryCouponReq) {
        return couponService.queryCoupon(queryCouponReq);
    }

    @RequestMapping(value = "/changeCouponStatus",method = RequestMethod.POST)
    @ResponseBody
    public ChangeCouponStatusRes changeCouponStatus(ChangeCouponStatusReq changeCouponStatusReq) {
        return couponService.changeCouponStatus(changeCouponStatusReq);
    }
}
