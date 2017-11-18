package com.keke.sanshui.admin.service;


import com.keke.sanshui.admin.request.agent.AgentQueryVo;
import com.keke.sanshui.admin.response.agent.AgentExportVo;
import com.keke.sanshui.admin.response.agent.UnderAgentVo;
import com.keke.sanshui.admin.response.agent.UnderPlayerVo;
import com.keke.sanshui.admin.vo.AgentVo;
import com.keke.sanshui.base.admin.dao.AgentPickTotalDAO;
import com.keke.sanshui.base.admin.dao.PlayerDAO;
import com.keke.sanshui.base.admin.dao.PlayerPickTotalDAO;
import com.keke.sanshui.base.admin.dao.PlayerRelationDAO;
import com.keke.sanshui.base.admin.po.*;
import com.keke.sanshui.base.admin.po.agent.AgentPo;
import com.keke.sanshui.base.admin.po.agent.AgentQueryPo;
import com.keke.sanshui.base.admin.service.AgentService;
import com.keke.sanshui.base.admin.service.PlayerCouponService;
import com.keke.sanshui.base.util.WeekUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class AdminAgentReadService {

    @Autowired
    AgentService agentService;

    @Autowired
    PlayerCouponService playerCouponService;

    @Autowired
    AgentPickTotalDAO agentPickTotalDAO;

    @Autowired
    PlayerPickTotalDAO playerPickTotalDAO;

    @Autowired
    PlayerRelationDAO playerRelationDAO;

    @Autowired
    PlayerDAO playerDAO;


    public List<AgentVo> selectAgentVoList(AgentQueryVo agentQueryVo) {
        AgentQueryPo queryAgentPo = new AgentQueryPo();
        queryAgentPo.setPlayerId(agentQueryVo.getGuid());
        queryAgentPo.setLimit(agentQueryVo.getLimit());
        queryAgentPo.setLevel(agentQueryVo.getLevel());
        queryAgentPo.setAgentName(agentQueryVo.getUserName());
        queryAgentPo.setAgentNickName(agentQueryVo.getNickName());
        queryAgentPo.setAgentWeChartNo(agentQueryVo.getWechartNo());
        queryAgentPo.setStatus(1);
        Integer page = agentQueryVo.getPage();
        if(page == 0){
            page = 1;
        }
        queryAgentPo.setOffset((page- 1)*agentQueryVo.getLimit());
        List<AgentPo> agentPos = agentService.selectList(queryAgentPo);
        Integer week =  agentQueryVo.getWeek();
        List<AgentVo> agentVos = agentPos.stream().map(eachAgentPo -> {
            AgentVo agentVo = new AgentVo();
            agentVo.setGameId(eachAgentPo.getPlayerId());
            agentVo.setName(eachAgentPo.getAgentName());
            agentVo.setType(eachAgentPo.getLevel());
            agentVo.setNickName(eachAgentPo.getAgentNickName());
            agentVo.setWeChartNo(eachAgentPo.getAgentWeChartNo());
            agentVo.setAgentId(eachAgentPo.getId());
            agentVo.setParentAgentId(eachAgentPo.getParentId());
            agentVo.setMemo(eachAgentPo.getMemo());
            agentVo.setWeek(agentQueryVo.getWeek().toString());
            return agentVo;

        }).map(agentVo -> {
            PlayerCouponPo playerCouponPo = playerCouponService.selectByPlayerId(agentVo.getGameId());
            if (playerCouponPo != null) {
                agentVo.setGoldCount(playerCouponPo.getGoldCount());
                agentVo.setSliverCount(playerCouponPo.getSilverCount());
            } else {
                agentVo.setGoldCount(0);
                agentVo.setSliverCount(0);
            }
            return agentVo;
        }).map(agentVo -> {
                    PlayerPickTotalPo playerPickTotalPo = playerPickTotalDAO.selectByPlayerId(agentVo.getGameId(), week);
                    if(playerPickTotalPo != null) {
                        agentVo.setAgentTotalPickUp(playerPickTotalPo.getTotalMoney() / 100);
                    }else{
                        agentVo.setAgentTotalPickUp(0L);
                    }
                    AgentPickTotalPo agentPickTotalPo = agentPickTotalDAO.selectByAgentId(agentVo.getAgentId(), week);
                    if(agentPickTotalPo != null) {
                        agentVo.setAgentUnderTotalPickUp(agentPickTotalPo.getTotalMoney());
                        agentVo.setAreaAgentUnderTotalPickUp(agentPickTotalPo.getTotalUnderMoney());
                    }else{
                        agentVo.setAgentUnderTotalPickUp(0L);
                        agentVo.setAreaAgentUnderTotalPickUp(0L);
                    }
                    return agentVo;
                }
        ).map(agentVo -> {
            Integer count = playerRelationDAO.selectUnderByPlayerId(agentVo.getGameId()).size();
            String underAgentCount = "-";
            agentVo.setMemberCount(count);
            if(agentVo.getType() == 2){
                AgentQueryPo tmpQueryVo = new AgentQueryPo();
                tmpQueryVo.setParentId(agentVo.getAgentId());
                underAgentCount = String.valueOf(agentService.selectList(tmpQueryVo).size());
            }
            agentVo.setUnderAgentCount(underAgentCount);
            return agentVo;
        }).collect(Collectors.toList());
        return agentVos;
    }

    public Long selectCount(AgentQueryVo agentQueryVo) {
        AgentQueryPo queryAgentPo = new AgentQueryPo();
        queryAgentPo.setPlayerId(agentQueryVo.getGuid());
        queryAgentPo.setLimit(agentQueryVo.getLimit());
        queryAgentPo.setAgentName(agentQueryVo.getUserName());
        queryAgentPo.setAgentNickName(agentQueryVo.getNickName());
        queryAgentPo.setAgentWeChartNo(agentQueryVo.getWechartNo());
        queryAgentPo.setStatus(1);
        return agentService.selectCount(queryAgentPo);
    }

    public List<UnderPlayerVo> obtainUnderPlayer(Integer agentGuid) {
        List<PlayerRelationPo> playerRelationPos = playerRelationDAO.selectUnderByPlayerId(agentGuid);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Integer week = WeekUtil.getCurrentWeek();
        return playerRelationPos.stream().map(playerRelationPo -> {
            UnderPlayerVo underPlayerVo = new UnderPlayerVo();
            Integer playerId = playerRelationPo.getPlayerId();
            PlayerPo playerPo = playerDAO.selectByPlayId(playerId);
            if(playerPo != null){
                underPlayerVo.setName(playerPo.getOtherName());
            }else{
                underPlayerVo.setName("");
            }
            if(playerPo.getGameInsertTime() != null) {
                underPlayerVo.setCreateTime(format.format(new Date(playerPo.getGameInsertTime())));
            }
            underPlayerVo.setPlayerGuid(playerRelationPo.getPlayerId());
            PlayerPickTotalPo playerPickTotalPo =  playerPickTotalDAO.selectByPlayerId(playerId,week);
            Long pickTotal = 0L;
            if(playerPickTotalPo != null){
                pickTotal = playerPickTotalPo.getTotalMoney();
            }
            underPlayerVo.setPlayerPickUp(pickTotal);
            return underPlayerVo;
        }).collect(Collectors.toList());
    }

    public List<UnderAgentVo> obtainUnderAgent(Integer agentId) {
        AgentQueryPo agentQueryPo = new AgentQueryPo();
        agentQueryPo.setParentId(agentId);
        agentQueryPo.setStatus(1);
        Integer week = WeekUtil.getCurrentWeek();
        List<AgentPo> agentPos = agentService.selectList(agentQueryPo);
        return agentPos.stream().map(agentPo -> {
            UnderAgentVo underAgentVo = new UnderAgentVo();
            underAgentVo.setAgentGuid(agentPo.getPlayerId());
            underAgentVo.setWechartNo(agentPo.getAgentWeChartNo());
            underAgentVo.setNickname(agentPo.getAgentNickName());
            underAgentVo.setAgentId(agentPo.getId());
            underAgentVo.setUsername(agentPo.getAgentName());

            PlayerPickTotalPo playerPickTotalPo = playerPickTotalDAO.selectByPlayerId(agentPo.getPlayerId(),week);
            if(playerPickTotalPo != null){
                underAgentVo.setProxyPickTotal(playerPickTotalPo.getTotalMoney());
            }else{
                underAgentVo.setProxyPickTotal(0L);
            }
            AgentPickTotalPo agentPickTotalPo = agentPickTotalDAO.selectByAgentId(agentPo.getId(),week);
            if(agentPickTotalPo != null){
                underAgentVo.setProxyAgentTotal(agentPickTotalPo.getTotalMoney());
            }else{
                underAgentVo.setProxyAgentTotal(0L);
            }
            return underAgentVo;
        }).collect(Collectors.toList());
    }

    public List<AgentExportVo> exportAgentPick(String week) {
        List<AgentExportVo> agentExportVos = agentPickTotalDAO.exportAgent(Integer.valueOf(week))
                .stream().map(agentPickTotalPo -> {
                    AgentExportVo agentExportVo = new AgentExportVo();
                    agentExportVo.setUnderAgentMoney(agentPickTotalPo.getTotalUnderMoney() == null ? 0L :agentPickTotalPo.getTotalUnderMoney());
                    agentExportVo.setUnderMonery(agentPickTotalPo.getTotalMoney() == null ?0L:agentPickTotalPo.getTotalMoney());
                    agentExportVo.setWeek(week);
                    AgentPo agentPo  = agentService.selectById(agentPickTotalPo.getAgentId());
                    if(agentPo != null) {
                        agentExportVo.setGuid(agentPo.getPlayerId());
                    }
                    return agentExportVo;
                }).collect(Collectors.toList());
        return agentExportVos;
    }
}
