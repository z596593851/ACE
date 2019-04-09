package com.hxm.trade.coupon.service;

import com.hxm.trade.common.protocol.coupon.ChangeCouponStatusReq;
import com.hxm.trade.common.protocol.coupon.ChangeCouponStatusRes;
import com.hxm.trade.common.protocol.coupon.QueryCouponReq;
import com.hxm.trade.common.protocol.coupon.QueryCouponRes;

public interface ICouponService {
    public QueryCouponRes queryCoupon(QueryCouponReq queryCouponReq);

    public ChangeCouponStatusRes changeCouponStatus(ChangeCouponStatusReq changeCouponStatusReq);
}
