package com.hxm.trade.order.service;

import com.hxm.trade.common.protocol.order.ConfirmOrderReq;
import com.hxm.trade.common.protocol.order.ConfirmOrderRes;

public interface IOrderService {
    public ConfirmOrderRes confirmOrder(ConfirmOrderReq confirmOrderReq);
}
