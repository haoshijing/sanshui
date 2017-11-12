package com.keke.sanshui.syncdata.canal.listener;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.google.common.collect.Maps;
import com.keke.sanshui.base.admin.dao.AgentDAO;
import com.keke.sanshui.base.admin.dao.PlayerRelationDAO;
import com.keke.sanshui.base.admin.po.agent.AgentPo;
import com.keke.sanshui.base.admin.service.PlayerService;
import com.keke.sanshui.syncdata.canal.event.UpdateCanalEvent;
import com.keke.sanshui.syncdata.canal.util.PlayerDataParser;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

//@Repository
@Slf4j
public class UpdateEventListener implements ApplicationListener<UpdateCanalEvent> {

    @Autowired
    private PlayerDataParser parser;

    @Autowired
    PlayerService playerService;

    @Autowired
    private AgentDAO agentDAO;

    @Autowired
    private PlayerRelationDAO playerRelationDAO;

    @Override
    public void onApplicationEvent(UpdateCanalEvent event) {
        CanalEntry.Entry entry = event.getEntry();
        String tableName = entry.getHeader().getTableName();

        if(!StringUtils.equals(tableName,"characters") || !StringUtils.equals(tableName,"world_records")){
            return;
        }
        if(StringUtils.equals(tableName,"characters") ){
            handlerCharactersData(entry);
        }else{
            handlerWorldData(entry);
        }
    }

    private void handlerWorldData(CanalEntry.Entry entry) {
        try {
            CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            if (rowChange != null) {
                List<CanalEntry.RowData> rowDataList = rowChange.getRowDatasList();
                rowDataList.stream().filter(rowData -> {
                    CanalEntry.Column column = rowData.getAfterColumns(0);
                    String typeVal = column.getValue();
                    if(StringUtils.equals("1",typeVal)){
                        return true;
                    }
                    return false;
                }).forEach(rowData -> {
                    CanalEntry.Column column = rowData.getAfterColumns(1);
                    String relationDatas  = column.getValue();
                    ByteBuf byteBuf = Unpooled.buffer(relationDatas.length());
                    for(int i = 0 ; i < relationDatas.length();i++){
                        char ch = relationDatas.charAt(i);
                        byteBuf.writeBytes(new byte[]{(byte)ch});
                    }
                   PlayerDataParser.PlayerAndAgentData playerAndAgentData = parser.parseFromWorldData(byteBuf.array());
                    playerAndAgentData.getPlayerRelationPos().forEach(playerRelationPo -> {
                        boolean isInDb = playerRelationDAO.queryByAgentAndPlayerGuid(playerRelationPo.getPlayerId().intValue(),
                                playerRelationPo.getPlayerId().intValue()) > 0;
                        if (!isInDb) {
                            playerRelationDAO.insertRelation(playerRelationPo);
                        }
                    });
                    playerAndAgentData.getAgentPos().forEach(agentPo -> {
                        AgentPo queryPo = agentDAO.selectById(agentPo.getPlayerId());
                        if(queryPo == null){
                            agentDAO.insert(agentPo);
                        }
                    });
                });
            }
        }catch (Exception e){
            log.error("",e);
        }
    }

    private void handlerCharactersData(CanalEntry.Entry entry) {
        try {
            CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            if (rowChange != null) {
                List<CanalEntry.RowData> rowDataList = rowChange.getRowDatasList();
                rowDataList.stream().map(rowData -> {
                    Map<String, Object> data = Maps.newHashMap();
                    rowData.getAfterColumnsList().forEach(column -> {
                        String name = column.getName();
                        if (StringUtils.equals(name, "base_data")) {
                            if (!column.getUpdated()) {
                               return;
                            }
                            String baseData = column.getValue();
                            ByteBuf byteBuf = Unpooled.buffer(baseData.length());
                            for(int i = 0 ; i < baseData.length();i++){
                                char ch = baseData.charAt(i);
                                byteBuf.writeBytes(new byte[]{(byte)ch});
                            }
                            data.put("base_data", byteBuf.array());
                        }
                        if (StringUtils.equals(name, "guid")) {
                            data.put("guid", Integer.valueOf(column.getValue()));
                        }
                    });
                    return data;
                }).forEach(data->{
                    if(data.get("base_data") != null) {
                        PlayerDataParser.PlayerInfo playerInfo = parser.parseFromBaseData(data);
                        if(playerInfo != null){
                            playerService.updatePlayerCoupon(playerInfo.getPlayerCouponPo());
                        }
                    }
                });

            }
        } catch (Exception e) {
            log.error("",e);
        }
    }
}
