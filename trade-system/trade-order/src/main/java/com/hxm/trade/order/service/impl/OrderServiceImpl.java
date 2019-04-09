package com.hxm.trade.order.service.impl;

import com.hxm.trade.common.api.ICouponApi;
import com.hxm.trade.common.api.IGoodsApi;
import com.hxm.trade.common.api.IUserApi;
import com.hxm.trade.common.constants.TradeEnums;
import com.hxm.trade.common.exception.AceOrderException;
import com.hxm.trade.common.protocol.coupon.ChangeCouponStatusReq;
import com.hxm.trade.common.protocol.coupon.ChangeCouponStatusRes;
import com.hxm.trade.common.protocol.coupon.QueryCouponReq;
import com.hxm.trade.common.protocol.coupon.QueryCouponRes;
import com.hxm.trade.common.protocol.goods.QueryGoodsReq;
import com.hxm.trade.common.protocol.goods.QueryGoodsRes;
import com.hxm.trade.common.protocol.order.ConfirmOrderReq;
import com.hxm.trade.common.protocol.order.ConfirmOrderRes;
import com.hxm.trade.common.protocol.user.ChangeUserMoneyReq;
import com.hxm.trade.common.protocol.user.ChangeUserMoneyRes;
import com.hxm.trade.common.protocol.user.QueryUserReq;
import com.hxm.trade.common.protocol.user.QueryUserRes;
import com.hxm.trade.common.util.IDGenerator;
import com.hxm.trade.dao.mapper.TradeOrderMapper;
import com.hxm.trade.dao.pojo.TradeOrder;
import com.hxm.trade.order.service.IOrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Date;

public class OrderServiceImpl implements IOrderService {

    @Autowired
    private IGoodsApi goodsApi;

    @Autowired
    private ICouponApi  couponApi;

    @Autowired
    private IUserApi userApi;

    @Autowired
    private TradeOrderMapper tradeOrderMapper;



    public ConfirmOrderRes confirmOrder(ConfirmOrderReq confirmOrderReq) {
        ConfirmOrderRes confirmOrderRes=new ConfirmOrderRes();
        confirmOrderRes.setRetCode(TradeEnums.RetEnum.SUCCESS.getCode());
        try{
            QueryGoodsReq queryGoodsReq=new QueryGoodsReq();
            queryGoodsReq.setGoodsId(confirmOrderReq.getGoodsId());
            QueryGoodsRes queryGoodsRes=goodsApi.queryGoods(queryGoodsReq);
            //1、参数校验
            checkConfirmOrderReq(confirmOrderReq,queryGoodsRes);
            //2、创建不可见订单
            String orderId=saveNoConfirmOrder(confirmOrderReq);
            //3、调用远程服务:使用优惠券、扣余额、扣库存
            callRemoteService(orderId,confirmOrderReq);


        }catch (Exception e){
            confirmOrderRes.setRetCode(TradeEnums.RetEnum.FAIL.getCode());
            confirmOrderRes.setRetInfo(e.getMessage());
        }


        return confirmOrderRes;
    }

    public void callRemoteService(String orderId,ConfirmOrderReq confirmOrderReq){
        try{
            //调用优惠券
            if(StringUtils.isNoneBlank(confirmOrderReq.getCouponId())){
                ChangeCouponStatusReq changeCouponStatusReq=new ChangeCouponStatusReq();
                changeCouponStatusReq.setCouponId(confirmOrderReq.getCouponId());
                changeCouponStatusReq.setIsUsed(TradeEnums.YesNoEnum.YES.getCode());
                changeCouponStatusReq.setOrderId(orderId);
                ChangeCouponStatusRes changeCouponStatusRes=couponApi.changeCouponStatus(changeCouponStatusReq);
                if(!changeCouponStatusRes.getRetCode().equals(TradeEnums.RetEnum.SUCCESS)){
                    throw new Exception("优惠券使用失败！");
                }

            }
            //扣余额
            if(confirmOrderReq.getMoneyPaid()!=null&&confirmOrderReq.getMoneyPaid().compareTo(BigDecimal.ZERO)==1){
                ChangeUserMoneyReq changeUserMoneyReq=new ChangeUserMoneyReq();
                changeUserMoneyReq.setOrderId(orderId);
                changeUserMoneyReq.setUserId(confirmOrderReq.getUserId());
                changeUserMoneyReq.setMoneyLogType(TradeEnums.UserMoneyLogTypeEnum.PAID.getCode());
                ChangeUserMoneyRes changeUserMoneyRes=userApi.changeUserMoney(changeUserMoneyReq);
                if(!changeUserMoneyRes.getRetCode().equals(TradeEnums.RetEnum.SUCCESS.getCode())){
                    throw new Exception("扣用户与余额失败！");
                }
            }
            //扣库存

        }catch (Exception e){
            //发送MQ消息
        }

    }

    public String  saveNoConfirmOrder(ConfirmOrderReq confirmOrderReq) throws Exception{
        TradeOrder tradeOrder = new TradeOrder();
        String orderId= IDGenerator.generateUUID();
        tradeOrder.setOrderId(orderId);
        tradeOrder.setUserId(confirmOrderReq.getUserId());
        tradeOrder.setOrderStatus(TradeEnums.OrderStatusEnum.NO_CONFIRM.getStatusCode());
        tradeOrder.setPayStatus(TradeEnums.PayStatusEnum.NO_PAY.getStatusCode());
        tradeOrder.setShippingStatus(TradeEnums.ShippingStatusEnum.NO_SHIP.getStatusCode());
        tradeOrder.setAddress(confirmOrderReq.getAddress());
        tradeOrder.setConsignee(confirmOrderReq.getConsignee());
        tradeOrder.setGoodsId(confirmOrderReq.getGoodsId());
        tradeOrder.setGoodsNumber(confirmOrderReq.getGoodsNumber());
        tradeOrder.setGoodsPrice(confirmOrderReq.getGoodsPrice());
        BigDecimal goodAmount=confirmOrderReq.getGoodsPrice().multiply(new BigDecimal(confirmOrderReq.getGoodsNumber()));
        tradeOrder.setGoodsAmount(goodAmount);
        BigDecimal shippingFee=calculateShippingFee(goodAmount);
        if(confirmOrderReq.getShippingFee().compareTo(shippingFee)!=0)
            throw new Exception("快递费用不正确！");
        tradeOrder.setShippingFee(shippingFee);
        BigDecimal orderAmount=goodAmount.add(shippingFee);
        if(orderAmount.compareTo(confirmOrderReq.getOrderAmount())!=0){
            throw new Exception("订单总价异常，请重新下单！");
        }
        tradeOrder.setOrderAmount(orderAmount);
        String couponId=confirmOrderReq.getCouponId();
        //优惠券不为空
        if(StringUtils.isNoneBlank(couponId)){
            QueryCouponReq queryCouponReq=new QueryCouponReq();
            queryCouponReq.setCouponId(couponId);
            QueryCouponRes queryCouponRes=couponApi.queryCoupon(queryCouponReq);
            if(queryCouponRes==null||!queryCouponRes.getRetCode().equals(TradeEnums.RetEnum.SUCCESS)){
                throw new Exception("优惠券非法");
            }
            if(!queryCouponRes.getIsUsed().equals(TradeEnums.YesNoEnum.NO.getCode())){
                throw new Exception("优惠券已使用");

            }
            tradeOrder.setCouponId(couponId);
            tradeOrder.setCouponPaid(queryCouponRes.getCouponPrice());
        }else {
            tradeOrder.setCouponPaid(BigDecimal.ZERO);
        }
        //MoneyPaid代表用户选择用余额支付多少
        if(confirmOrderReq.getMoneyPaid()!=null){
            int r=confirmOrderReq.getMoneyPaid().compareTo(BigDecimal.ZERO);
            if(r==-1){
                throw new Exception("余额金额非法");
            }
            if(r==1){
                QueryUserReq queryUserReq=new QueryUserReq();
                queryUserReq.setUserId(confirmOrderReq.getUserId());
                QueryUserRes queryUserRes=userApi.queryUserById(queryUserReq);
                if(queryUserReq==null||!queryUserRes.getRetCode().equals(TradeEnums.RetEnum.SUCCESS)){
                    throw new Exception("用户非法");
                }
                if(queryUserRes.getUserMoney().compareTo(confirmOrderReq.getMoneyPaid())==-1){
                    throw new Exception("余额不足");
                }
                tradeOrder.setMoneyPaid(confirmOrderReq.getMoneyPaid());
            }

        }else {
            tradeOrder.setMoneyPaid(BigDecimal.ZERO);
        }
        BigDecimal payAmount=orderAmount.subtract(tradeOrder.getMoneyPaid()).subtract(tradeOrder.getCouponPaid());
        tradeOrder.setPayAmount(payAmount);
        tradeOrder.setAddTime(new Date());

        int ret=tradeOrderMapper.insert(tradeOrder);
        if(ret!=1){
            throw new Exception("保存不可见订单失效");
        }
        return orderId;

    }

    private void checkConfirmOrderReq(ConfirmOrderReq confirmOrderReq,QueryGoodsRes queryGoodsRes){
        if(confirmOrderReq==null){
            throw new AceOrderException("下单信息不能为空");
        }
        if(confirmOrderReq.getUserId()==null){
            throw new AceOrderException("会员账号不能为空");
        }
        if(confirmOrderReq.getGoodsId()==null){
            throw new AceOrderException("商品编号不能为空");
        }
        if(confirmOrderReq.getGoodsNumber()==null||confirmOrderReq.getGoodsNumber()<=0){
            throw new AceOrderException("购买数量不能小于0");
        }
        if(confirmOrderReq.getAddress()==null){
            throw new AceOrderException("收货地址不能为空");
        }
        if(queryGoodsRes==null||!queryGoodsRes.getRetCode().equals(TradeEnums.RetEnum.SUCCESS.getCode())){
            throw new AceOrderException("未查询到该商品("+confirmOrderReq.getGoodsId()+")");
        }
        if(queryGoodsRes.getGoodsNumber()<confirmOrderReq.getGoodsNumber()){
            throw new AceOrderException("商品库存不足");
        }
        if(queryGoodsRes.getGoodsPrice().compareTo(confirmOrderReq.getGoodsPrice())!=0){
            throw new AceOrderException("当前商品价格有变化，请重新下单");
        }
        if(confirmOrderReq.getShippingFee()==null){
            confirmOrderReq.setShippingFee(BigDecimal.ZERO);
        }
        if(confirmOrderReq.getOrderAmount()==null){
            confirmOrderReq.setOrderAmount(BigDecimal.ZERO);
        }
    }

    private BigDecimal calculateShippingFee(BigDecimal goodsAmount){
        if(goodsAmount.doubleValue()>100.00){
            return BigDecimal.ZERO;

        }else {
            return new BigDecimal("10");
        }
    }
}
