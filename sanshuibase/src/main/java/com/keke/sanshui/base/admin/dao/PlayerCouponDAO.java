package com.keke.sanshui.base.admin.dao;

import com.keke.sanshui.base.admin.po.PlayerCouponPo;
import org.apache.ibatis.annotations.Param;

public interface PlayerCouponDAO {

    PlayerCouponPo selectByPlayerId(Integer playerId);

    int insertPlayerCouponPo(@Param("param") PlayerCouponPo playerCouponPo);

    int updatePlayerCouponPo(@Param("param") PlayerCouponPo playerCouponPo);
}
