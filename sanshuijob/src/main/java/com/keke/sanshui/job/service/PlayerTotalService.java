package com.keke.sanshui.job.service;

import com.keke.sanshui.base.admin.dao.PlayerPickTotalDAO;
import com.keke.sanshui.base.admin.po.PlayerPickTotalPo;
import com.keke.sanshui.base.admin.po.PlayerPo;
import com.keke.sanshui.base.admin.service.OrderService;
import com.keke.sanshui.base.admin.service.PlayerService;
import com.keke.sanshui.base.util.WeekUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author haoshijing
 * @version 2017年11月03日 13:23
 **/
@Repository
@Slf4j
public class PlayerTotalService {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PlayerPickTotalDAO playerPickTotalDAO;

    private final int BATCH_SIZE = 1000;

    public void work(){
        log.info("开始统计充值额");
        List<PlayerPo> playerPoList = playerService.selectList(0,BATCH_SIZE);
        Integer nextMaxId = 0;
        do{
            playerPoList.forEach(playerPo -> {
                Integer playerId = playerPo.getPlayerId();
                long weekStartTimestamp = WeekUtil.getWeekStartTimestamp();
                long weekEndTimestamp = WeekUtil.getWeekEndTimestamp();
                int week = WeekUtil.getCurrentWeek();
                Long sumPickUp = orderService.queryPickupSum(playerId,weekStartTimestamp,weekEndTimestamp);
                if(sumPickUp != null && sumPickUp > 0){
                    PlayerPickTotalPo playerPickTotalPo =  playerPickTotalDAO.selectByPlayerId(playerId,week);
                    if(playerPickTotalPo != null){
                        PlayerPickTotalPo updatePickTotalPo = new PlayerPickTotalPo();
                        updatePickTotalPo.setLastUpdateTime(System.currentTimeMillis());
                        updatePickTotalPo.setTotalMoney(sumPickUp);
                        updatePickTotalPo.setId(playerPickTotalPo.getId());
                        int ret = playerPickTotalDAO.updateTotalPo(updatePickTotalPo);

                    }else{
                        PlayerPickTotalPo newPlayerPickTotalPo = new PlayerPickTotalPo();
                        newPlayerPickTotalPo.setTotalMoney(sumPickUp);
                        newPlayerPickTotalPo.setLastUpdateTime(System.currentTimeMillis());
                        newPlayerPickTotalPo.setPlayerId(playerId);
                        newPlayerPickTotalPo.setWeek(week);
                        playerPickTotalDAO.insertTotalPo(newPlayerPickTotalPo);
                    }
                }

            });
            playerPoList = playerService.selectList(nextMaxId,BATCH_SIZE);
            if(playerPoList.size() > 0){
                nextMaxId = playerPoList.get(playerPoList.size()-1).getId();
            }
        }while (playerPoList.size() != 0);

    }
}
