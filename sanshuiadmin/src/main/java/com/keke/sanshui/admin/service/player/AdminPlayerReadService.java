package com.keke.sanshui.admin.service.player;


import com.keke.sanshui.admin.request.player.PlayerQueryVo;
import com.keke.sanshui.admin.response.player.PlayerResponseVo;
import com.keke.sanshui.base.admin.dao.PlayerCouponDAO;
import com.keke.sanshui.base.admin.dao.PlayerDAO;
import com.keke.sanshui.base.admin.dao.PlayerPickTotalDAO;
import com.keke.sanshui.base.admin.dao.PlayerRelationDAO;
import com.keke.sanshui.base.admin.po.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AdminPlayerReadService {

    @Autowired
    PlayerDAO playerDAO;

    @Autowired
    PlayerCouponDAO playerCouponDAO;

    @Autowired
    PlayerPickTotalDAO playerPickTotalDAO;

    @Autowired
    PlayerRelationDAO playerRelationDAO;

    public List<PlayerResponseVo> queryList(PlayerQueryVo playerQueryVo) {
        QueryPlayerPo queryPlayerPo = convert2QueryPo(playerQueryVo);
        List<PlayerPo> playerPos = playerDAO.queryList(queryPlayerPo);
        return playerPos.stream().map(playerPo -> {
            PlayerResponseVo playerResponseVo = new PlayerResponseVo();
            PlayerCouponPo playerCouponPo = playerCouponDAO.selectByPlayerId(playerPo.getPlayerId());
            if(playerCouponPo != null){
                playerResponseVo.setSliverCount(playerCouponPo.getSilverCount());
                playerResponseVo.setGoldCount(playerCouponPo.getGoldCount());
            }
            playerResponseVo.setGuid(playerPo.getPlayerId());
            playerResponseVo.setInsertTime(playerPo.getGameInsertTime());
            playerResponseVo.setOtherName(playerPo.getOtherName());
            PlayerPickTotalPo playerPickTotalPo = playerPickTotalDAO.selectByPlayerId(playerPo.getPlayerId(),playerQueryVo.getWeek());
            if(playerPickTotalPo != null){
                playerResponseVo.setPickTotal(playerPickTotalPo.getTotalMoney());
            }else{
                playerResponseVo.setPickTotal(0L);
            }
            PlayerRelationPo playerRelationPo =  playerRelationDAO.selectByPlayerId(playerPo.getPlayerId());
            if(playerRelationPo != null){
                playerResponseVo.setAgentGuidId(playerRelationPo.getParentPlayerId());
            }else{
                playerResponseVo.setAgentGuidId(0);
            }
            playerResponseVo.setWeek(playerQueryVo.getWeek().toString());
            return playerResponseVo;
        }).collect(Collectors.toList());
    }

    public Long queryCount(PlayerQueryVo playerQueryVo) {
        return playerDAO.queryCount(convert2QueryPo(playerQueryVo));
    }

    private QueryPlayerPo convert2QueryPo(PlayerQueryVo playerQueryVo){
        QueryPlayerPo queryPlayerPo = new QueryPlayerPo();
        queryPlayerPo.setLimit(playerQueryVo.getLimit());
        queryPlayerPo.setOffset((playerQueryVo.getPage()-1)*playerQueryVo.getLimit());
        queryPlayerPo.setPlayerId(playerQueryVo.getGuid());
        return queryPlayerPo;
    }
}
