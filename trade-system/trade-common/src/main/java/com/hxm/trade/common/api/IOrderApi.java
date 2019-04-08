package com.hxm.trade.common.api;

import com.hxm.trade.common.protocol.order.ConfirmOrderReq;
import com.hxm.trade.common.protocol.order.ConfirmOrderRes;

public interface IOrderApi {
    public ConfirmOrderRes confirmOrder(ConfirmOrderReq confirmOrderReq);
}
