package com.hxm.trade.dao.mapper;

import com.hxm.trade.dao.pojo.TradeCoupon;
import com.hxm.trade.dao.pojo.TradeCouponExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TradeCouponMapper {
    int countByExample(TradeCouponExample example);

    int deleteByExample(TradeCouponExample example);

    int deleteByPrimaryKey(String couponId);

    int insert(TradeCoupon record);

    int insertSelective(TradeCoupon record);

    List<TradeCoupon> selectByExample(TradeCouponExample example);

    TradeCoupon selectByPrimaryKey(String couponId);

    int updateByExampleSelective(@Param("record") TradeCoupon record, @Param("example") TradeCouponExample example);

    int updateByExample(@Param("record") TradeCoupon record, @Param("example") TradeCouponExample example);

    int updateByPrimaryKeySelective(TradeCoupon record);

    int updateByPrimaryKey(TradeCoupon record);
}