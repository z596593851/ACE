package com.hxm.trade.common.api;

import com.hxm.trade.common.protocol.coupon.ChangeCouponStatusReq;
import com.hxm.trade.common.protocol.coupon.ChangeCouponStatusRes;
import com.hxm.trade.common.protocol.coupon.QueryCouponReq;
import com.hxm.trade.common.protocol.coupon.QueryCouponRes;

public interface ICouponApi {
    public QueryCouponRes queryCoupon(QueryCouponReq queryCouponReq);
    public ChangeCouponStatusRes changeCouponStatus(ChangeCouponStatusReq changeCouponStatusReq);
}
