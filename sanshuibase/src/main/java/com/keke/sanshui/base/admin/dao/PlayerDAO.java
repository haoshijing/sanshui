package com.keke.sanshui.base.admin.dao;

import com.keke.sanshui.base.admin.po.PlayerPo;
import com.keke.sanshui.base.admin.po.QueryPlayerPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PlayerDAO {
    /**
     *
     * @param playerPo
     * @return
     */
    int insertPlayer(@Param("player") PlayerPo playerPo);

    int updatePlayer(@Param("param") PlayerPo playerPo);

    Long queryCount();

    PlayerPo selectByPlayId(Integer playerId);

    List<PlayerPo> queryPlayerList(@Param("playerId") Integer lastPlayerId, @Param("limit") Integer limit);

    List<PlayerPo> queryList(@Param("param")QueryPlayerPo queryPlayerPo);

    Long queryCount(@Param("param")QueryPlayerPo queryPlayerPo);
}
