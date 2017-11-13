package com.keke.sanshui.base.admin.dao;

import com.keke.sanshui.base.admin.po.PlayerPickTotalPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author haoshijing
 * @version 2017年11月03日 12:08
 **/
public interface PlayerPickTotalDAO {

    PlayerPickTotalPo selectByPlayerId(@Param("playerId") Integer playerId,@Param("week") Integer week);

    PlayerPickTotalPo batchSelect(@Param("playerIds") List<Integer> playerIds,@Param("week") Integer week);

    void insertTotalPo(@Param("param") PlayerPickTotalPo playerPickTotalPo);

    int updateTotalPo(@Param("param") PlayerPickTotalPo playerPickTotalPo);

    /*
     * 某一周内的所有总和
     * @param playerIds
     * @param week
     * @return
     */
    Long sumPickUp(@Param("playerIds") List<Integer> playerIds, @Param("week") Integer week);
}
