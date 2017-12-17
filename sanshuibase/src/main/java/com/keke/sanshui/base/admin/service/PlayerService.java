package com.keke.sanshui.base.admin.service;

import com.keke.sanshui.base.admin.dao.PlayerCouponDAO;
import com.keke.sanshui.base.admin.dao.PlayerDAO;
import com.keke.sanshui.base.admin.po.PlayerCouponPo;
import com.keke.sanshui.base.admin.po.PlayerPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {
    @Autowired
    private PlayerDAO playerDAO;

    @Autowired
    PlayerCouponDAO playerCouponDAO;

    public List<PlayerPo> selectList(Integer maxPlayerId, Integer limit){
       return playerDAO.queryPlayerList(maxPlayerId,limit);
    }

    public void updatePlayer(PlayerPo playerPo){
        playerDAO.updatePlayer(playerPo);
    }

    public void updatePlayerCoupon(PlayerCouponPo playerCouponPo){
        playerCouponDAO.updatePlayerCouponPo(playerCouponPo);
    }

    public void insertPlayer(PlayerPo playerPo){
      PlayerPo queryPlayerPo =   playerDAO.selectByPlayId(playerPo.getPlayerId());
      if(queryPlayerPo == null){
          playerDAO.insertPlayer(playerPo);
      }
    }

    public boolean checkPlayerExsist(Integer playerId){
        return playerDAO.selectByPlayId(playerId) != null;
    }

    public void insertPlayerCoupon(PlayerCouponPo playerCouponPo){
        PlayerCouponPo queryPlayerCoupon = playerCouponDAO.selectByPlayerId(playerCouponPo.getPlayerId());
        if(null == queryPlayerCoupon){
            playerCouponDAO.insertPlayerCouponPo(playerCouponPo);
        }
    }

    public PlayerPo selectById(Integer guid) {
        return playerDAO.selectByPlayId(guid);
    }
}
