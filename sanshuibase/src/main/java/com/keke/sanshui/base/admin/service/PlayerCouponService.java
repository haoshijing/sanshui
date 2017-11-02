package com.keke.sanshui.base.admin.service;

import com.keke.sanshui.base.admin.dao.PlayerCouponDAO;
import com.keke.sanshui.base.admin.po.PlayerCouponPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerCouponService {

    @Autowired
    private PlayerCouponDAO playerCouponDAO;

    public PlayerCouponPo selectByPlayerId(Integer playerId){
        return playerCouponDAO.selectByPlayerId(playerId);
    }
}
