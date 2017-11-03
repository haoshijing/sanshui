package com.keke.sanshui.base.admin.dao;

import com.keke.sanshui.base.admin.po.PlayerPickTotalPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author haoshijing
 * @version 2017年11月03日 12:08
 **/
public interface PlayerPickTotalDAO {

    PlayerPickTotalPo selectByPlayerId(Integer playerId,Integer week);

    void insertTotalPo(@Param("param") PlayerPickTotalPo playerPickTotalPo);

    int updateTotalPo(@Param("param") PlayerPickTotalPo playerPickTotalPo);

    /*
     * 某一周内的所有总和
     * @param playerIds
     * @param week
     * @return
     */
    Long sumPickUp(List<Integer> playerIds, Integer week);
}
