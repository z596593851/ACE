package com.hxm.trade.common.api;

import com.hxm.trade.common.protocol.goods.QueryGoodsReq;
import com.hxm.trade.common.protocol.goods.QueryGoodsRes;
import com.hxm.trade.common.protocol.goods.ReduceGoodsNumberReq;
import com.hxm.trade.common.protocol.goods.ReduceGoodsNumberRes;

public interface IGoodsApi {
    public QueryGoodsRes queryGoods(QueryGoodsReq queryGoodsReq);
    public ReduceGoodsNumberRes reduceGoodsNumber(ReduceGoodsNumberReq reduceGoodsNumberReq);
}
