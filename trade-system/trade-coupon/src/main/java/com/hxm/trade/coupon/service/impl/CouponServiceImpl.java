package com.hxm.trade.coupon.service.impl;

import com.hxm.trade.common.constants.TradeEnums;
import com.hxm.trade.common.protocol.coupon.ChangeCouponStatusReq;
import com.hxm.trade.common.protocol.coupon.ChangeCouponStatusRes;
import com.hxm.trade.common.protocol.coupon.QueryCouponReq;
import com.hxm.trade.common.protocol.coupon.QueryCouponRes;
import com.hxm.trade.coupon.service.ICouponService;
import com.hxm.trade.dao.mapper.TradeCouponMapper;
import com.hxm.trade.dao.pojo.TradeCoupon;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

public class CouponServiceImpl implements ICouponService {
    private TradeCouponMapper tradeCouponMapper;
    public QueryCouponRes queryCoupon(QueryCouponReq queryCouponReq) {
        QueryCouponRes queryCouponRes=new QueryCouponRes();
        queryCouponRes.setRetCode(TradeEnums.RetEnum.SUCCESS.getCode());
        queryCouponRes.setRetInfo(TradeEnums.RetEnum.SUCCESS.getDesc());
        try{
            if(queryCouponReq==null|| StringUtils.isNoneBlank(queryCouponReq.getCouponId())){
                throw new Exception("请求参数不正确，优惠券编号为空");
            }
            TradeCoupon tradeCoupon=tradeCouponMapper.selectByPrimaryKey(queryCouponReq.getCouponId());
            if(tradeCoupon!=null){
                BeanUtils.copyProperties(tradeCoupon,queryCouponRes);
            }else {
                throw new Exception();
            }
        }catch (Exception e){

        }
        return null;
    }

    public ChangeCouponStatusRes changeCouponStatus(ChangeCouponStatusReq changeCouponStatusReq) {
        return null;
    }
}
