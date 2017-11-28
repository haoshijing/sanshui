package com.keke.sanshui.admin.service.player;


import com.google.common.collect.Lists;
import com.keke.sanshui.admin.request.player.PlayerPickRequest;
import com.keke.sanshui.admin.request.player.PlayerQueryVo;
import com.keke.sanshui.admin.response.player.PlayerPickResponseVo;
import com.keke.sanshui.admin.response.player.PlayerResponseVo;
import com.keke.sanshui.base.admin.dao.PlayerCouponDAO;
import com.keke.sanshui.base.admin.dao.PlayerDAO;
import com.keke.sanshui.base.admin.dao.PlayerPickTotalDAO;
import com.keke.sanshui.base.admin.dao.PlayerRelationDAO;
import com.keke.sanshui.base.admin.po.*;
import com.keke.sanshui.base.admin.po.order.Order;
import com.keke.sanshui.base.admin.po.order.QueryOrderPo;
import com.keke.sanshui.base.admin.service.OrderService;
import com.keke.sanshui.base.util.WeekUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.Comparator;
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

    @Autowired
    private OrderService orderService;

    public List<PlayerResponseVo> queryList(PlayerQueryVo playerQueryVo) {
        QueryPlayerPo queryPlayerPo = convert2QueryPo(playerQueryVo);
        List<PlayerPo> playerPos = playerDAO.queryList(queryPlayerPo);
        return playerPos.stream().map(playerPo -> {
            PlayerResponseVo playerResponseVo = new PlayerResponseVo();
            PlayerCouponPo playerCouponPo = playerCouponDAO.selectByPlayerId(playerPo.getPlayerId());
            if (playerCouponPo != null) {
                playerResponseVo.setSliverCount(playerCouponPo.getSilverCount());
                playerResponseVo.setGoldCount(playerCouponPo.getGoldCount());
            }
            playerResponseVo.setGuid(playerPo.getPlayerId());
            playerResponseVo.setInsertTime(playerPo.getGameInsertTime());
            playerResponseVo.setOtherName(playerPo.getOtherName());
            PlayerPickTotalPo playerPickTotalPo = playerPickTotalDAO.selectByPlayerId(playerPo.getPlayerId(), playerQueryVo.getWeek());
            if (playerPickTotalPo != null) {
                playerResponseVo.setPickTotal(playerPickTotalPo.getTotalMoney());
            } else {
                playerResponseVo.setPickTotal(0L);
            }
            PlayerRelationPo playerRelationPo = playerRelationDAO.selectByPlayerId(playerPo.getPlayerId());
            if (playerRelationPo != null) {
                playerResponseVo.setAgentGuidId(playerRelationPo.getParentPlayerId());
            } else {
                playerResponseVo.setAgentGuidId(0);
            }
            playerResponseVo.setWeek(playerQueryVo.getWeek().toString());
            return playerResponseVo;
        }).collect(Collectors.toList());
    }

    public List<PlayerResponseVo> queryAgentListList(PlayerQueryVo playerQueryVo) {
        QueryPlayerRelationPo  relationPo = new QueryPlayerRelationPo();
        relationPo.setLimit(playerQueryVo.getLimit());
        relationPo.setParentPlayerId(playerQueryVo.getParentGuid());
        relationPo.setOffset((playerQueryVo.getPage() - 1) * playerQueryVo.getLimit());
        relationPo.setPlayerId(playerQueryVo.getGuid());
        List<PlayerRelationPo> playerRelationPos = playerRelationDAO.queryList(relationPo);
        return playerRelationPos.stream().map(playerRelationPo -> {
            PlayerResponseVo playerResponseVo = new PlayerResponseVo();
            PlayerCouponPo playerCouponPo = playerCouponDAO.selectByPlayerId(playerRelationPo.getPlayerId());
            if (playerCouponPo != null) {
                playerResponseVo.setSliverCount(playerCouponPo.getSilverCount());
                playerResponseVo.setGoldCount(playerCouponPo.getGoldCount());
            }
            PlayerPo playerPo = playerDAO.selectByPlayId(playerRelationPo.getPlayerId());
            playerResponseVo.setGuid(playerRelationPo.getPlayerId());
            playerResponseVo.setInsertTime(playerPo.getGameInsertTime());
            playerResponseVo.setOtherName(playerPo.getOtherName());
            PlayerPickTotalPo playerPickTotalPo = playerPickTotalDAO.selectByPlayerId(playerPo.getPlayerId(), playerQueryVo.getWeek());
            if (playerPickTotalPo != null) {
                playerResponseVo.setPickTotal(playerPickTotalPo.getTotalMoney());
            } else {
                playerResponseVo.setPickTotal(0L);
            }
            playerResponseVo.setAgentGuidId(playerRelationPo.getParentPlayerId());
            playerResponseVo.setWeek(playerQueryVo.getWeek().toString());
            return playerResponseVo;
        }).collect(Collectors.toList());
    }

    public Long queryAgentCount(PlayerQueryVo playerQueryVo){
        QueryPlayerRelationPo  relationPo = new QueryPlayerRelationPo();
        relationPo.setLimit(playerQueryVo.getLimit());
        relationPo.setParentPlayerId(playerQueryVo.getParentGuid());
        relationPo.setOffset((playerQueryVo.getPage() - 1) * playerQueryVo.getLimit());
        relationPo.setPlayerId(playerQueryVo.getGuid());
        return  playerRelationDAO.queryCount(relationPo);
    }


    public Long queryCount(PlayerQueryVo playerQueryVo) {
        return playerDAO.queryCount(convert2QueryPo(playerQueryVo));
    }

    private QueryPlayerPo convert2QueryPo(PlayerQueryVo playerQueryVo) {
        QueryPlayerPo queryPlayerPo = new QueryPlayerPo();
        queryPlayerPo.setLimit(playerQueryVo.getLimit());
        queryPlayerPo.setParentGuid(playerQueryVo.getParentGuid());
        queryPlayerPo.setOffset((playerQueryVo.getPage() - 1) * playerQueryVo.getLimit());
        queryPlayerPo.setPlayerId(playerQueryVo.getGuid());
        return queryPlayerPo;
    }

    public List<PlayerPickResponseVo> queryPickList(PlayerPickRequest playerPickRequest) {
        Integer playerId = playerPickRequest.getGuid();
        Integer week = playerPickRequest.getWeek();
        Long startTimestamp = WeekUtil.getWeekStartTimestamp(week);
        Long endTimestamp = WeekUtil.getWeekEndTimestamp(week);

        QueryOrderPo queryOrderPo = new QueryOrderPo();
        queryOrderPo.setOrderStatus(2);
        queryOrderPo.setLimit(10000);
        queryOrderPo.setOffset(0);
        queryOrderPo.setClientGuids(Lists.newArrayList(playerId));
        queryOrderPo.setStartTimestamp(startTimestamp);
        queryOrderPo.setEndTimestamp(endTimestamp);
        SimpleDateFormat format  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Order> list =  orderService.selectList(queryOrderPo);
        List<PlayerPickResponseVo> playerPickResponseVos = list.stream().map(order -> {
            PlayerPickResponseVo playerPickResponseVo = new PlayerPickResponseVo();
            playerPickResponseVo.setGuid(order.getClientGuid());
            playerPickResponseVo.setMoney(Integer.valueOf(order.getPrice()) /100);
            playerPickResponseVo.setOrderStatus(order.getOrderStatus());
            playerPickResponseVo.setOrderStatusStr(order.getOrderStatus() == 1 ? "未支付":"已支付");
            playerPickResponseVo.setOrderTime(format.format(order.getInsertTime()));
            return playerPickResponseVo;
        }).collect(Collectors.toList());
        playerPickResponseVos.sort(new Comparator<PlayerPickResponseVo>() {
            @Override
            public int compare(PlayerPickResponseVo o1, PlayerPickResponseVo o2) {
                if(o1.getOrderStatus() > o2.getOrderStatus()){
                    return 1;
                }
                if(o1.getOrderStatus() == o2.getOrderStatus()){
                    if(o1.getMoney() > o2.getMoney()){
                        return 1;
                    }
                }
                return 0;
            }
        });
        return playerPickResponseVos;
    }

    public List<PlayerPickResponseVo> queryPickListForAgent(PlayerPickRequest playerPickRequest) {
        Integer agentGuid = playerPickRequest.getGuid();
        List<PlayerRelationPo> playerRelationPos = playerRelationDAO.selectUnderByPlayerId(agentGuid);
        List<Integer> guids = playerRelationPos.stream().map(playerRelationPo -> {
            return playerRelationPo.getPlayerId();
        }).collect(Collectors.toList());
        Integer week = playerPickRequest.getWeek();
        Long startTimestamp = WeekUtil.getWeekStartTimestamp(week);
        Long endTimestamp = WeekUtil.getWeekEndTimestamp(week);

        QueryOrderPo queryOrderPo = new QueryOrderPo();
        queryOrderPo.setOrderStatus(2);
        queryOrderPo.setLimit(10000);
        queryOrderPo.setOffset(0);
        queryOrderPo.setClientGuids(Lists.newArrayList(guids));
        queryOrderPo.setStartTimestamp(startTimestamp);
        queryOrderPo.setEndTimestamp(endTimestamp);
        SimpleDateFormat format  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Order> list =  orderService.selectList(queryOrderPo);
        List<PlayerPickResponseVo> playerPickResponseVos = list.stream().map(order -> {
            PlayerPickResponseVo playerPickResponseVo = new PlayerPickResponseVo();
            PlayerPo playerPo = playerDAO.selectByPlayId(order.getClientGuid());
            playerPickResponseVo.setGuid(order.getClientGuid());
            playerPickResponseVo.setMoney(Integer.valueOf(order.getPrice()) /100);
            playerPickResponseVo.setOrderStatus(order.getOrderStatus());
            if(playerPo != null) {
                playerPickResponseVo.setOtherName(playerPo.getOtherName());
            }
            playerPickResponseVo.setOrderStatusStr(order.getOrderStatus() == 1 ? "未支付":"已支付");
            playerPickResponseVo.setOrderTime(format.format(order.getInsertTime()));
            return playerPickResponseVo;
        }).collect(Collectors.toList());
        return playerPickResponseVos;
    }
}
