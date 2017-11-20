package com.keke.sanshui.base.admin.dao;

import com.keke.sanshui.base.admin.po.PlayerRelationPo;
import com.keke.sanshui.base.admin.po.QueryPlayerRelationPo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author haoshijing
 * @version 2017年11月03日 12:36
 **/
public interface PlayerRelationDAO {

     void insertRelation(@Param("param") PlayerRelationPo playerRelationPo);

     PlayerRelationPo selectByPlayerId(Integer playerId);

     List<PlayerRelationPo> selectUnderByPlayerId(Integer playerId);

     List<PlayerRelationPo> selectAll();

     List<PlayerRelationPo> queryList(@Param("param") QueryPlayerRelationPo queryPlayerRelationPo);

     Long queryCount(@Param("param") QueryPlayerRelationPo queryPlayerRelationPo);

     int updatePlayerRelation(@Param("param")PlayerRelationPo playerRelationPo);

     Integer queryByAgentAndPlayerGuid(@Param("parentPlayerId") Integer agentIdGuid,@Param("playerId") Integer playerGuid);

     int deleteRelation(int id);
}
