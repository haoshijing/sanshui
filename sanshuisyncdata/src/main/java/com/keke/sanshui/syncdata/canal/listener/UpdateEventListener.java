package com.keke.sanshui.syncdata.canal.listener;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.google.common.collect.Maps;
import com.keke.sanshui.base.admin.po.PlayerPo;
import com.keke.sanshui.base.admin.service.PlayerService;
import com.keke.sanshui.syncdata.canal.event.UpdateCanalEvent;
import com.keke.sanshui.syncdata.canal.util.PlayerDataParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Repository;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class UpdateEventListener implements ApplicationListener<UpdateCanalEvent> {

    @Autowired
    private PlayerDataParser parser;

    @Autowired
    PlayerService playerService;

    @Override
    public void onApplicationEvent(UpdateCanalEvent event) {
        CanalEntry.Entry entry = event.getEntry();
        String tableName = entry.getHeader().getTableName();
        if(!StringUtils.equals(tableName,"characters")){
            return;
        }
        try {
            CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            if (rowChange != null) {
                List<CanalEntry.RowData> rowDataList = rowChange.getRowDatasList();
                rowDataList.stream().map(rowData -> {
                    Map<String, Object> data = Maps.newHashMap();
                    rowData.getAfterColumnsList().stream().forEach(column -> {
                        String name = column.getName();
                        if (StringUtils.equals(name, "base_data")) {
//                            if (!column.getUpdated()) {
//                                return;
//                            }
                            String baseData = column.getValue();
                            ByteBuffer byteBuffer = ByteBuffer.allocate(baseData.length());
                            for(int i = 0 ; i < baseData.length();i++){
                                char ch = baseData.charAt(i);
                                byteBuffer.put((byte)ch);
                            }
                            data.put("base_data", byteBuffer.array());
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
            e.printStackTrace();
        }
    }
}
