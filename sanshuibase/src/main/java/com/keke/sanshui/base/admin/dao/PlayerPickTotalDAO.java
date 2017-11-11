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

    void insertTotalPo(@Param("param") PlayerPickTotalPo playerPickTotalPo);

    int updateTotalPo(@Param("param") PlayerPickTotalPo playerPickTotalPo);

    /*
     * 某一周内的所有总和
     * @param playerIds
     * @param week
     * @return
     */
<<<<<<< b840df4c9e00a12e38e502c26442d4e7ead23540
    Long sumPickUp(@Param("playerIds") List<Integer> playerIds,@Param("week") Integer week);
=======
    Long sumPickUp(@Param("playerIds") List<Integer> playerIds, @Param("week") Integer week);
>>>>>>> 后台统计代码功能
}
