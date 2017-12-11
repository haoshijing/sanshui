package com.keke.sanshui.syncdata.canal.listener;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.google.common.collect.Maps;
import com.keke.sanshui.base.admin.service.PlayerService;
import com.keke.sanshui.syncdata.canal.event.InsertCanalEvent;
import com.keke.sanshui.syncdata.canal.util.PlayerDataParser;
import com.keke.sanshui.syncdata.canal.util.PlayerDataParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;

import java.util.List;
import java.util.Map;


@Slf4j
//@Repository
public class InsertEventListener implements ApplicationListener<InsertCanalEvent> {

    @Autowired
    PlayerService playerService;
    @Autowired
    PlayerDataParser parser;

    @Override
    public void onApplicationEvent(InsertCanalEvent event) {
        CanalEntry.Entry entry = event.getEntry();
        try {
            String tableName = entry.getHeader().getTableName();
            if(!StringUtils.equals(tableName,"characters")){
                return;
            }
            CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            if (rowChange != null) {
                List<CanalEntry.RowData> rowDataList = rowChange.getRowDatasList();
                rowDataList.stream().map(rowData -> {
                    Map<String, Object> data = Maps.newHashMap();
                    rowData.getAfterColumnsList().stream().forEach(column -> {
                        String name = column.getName();
                        if (StringUtils.equals(name, "base_data")) {
                            String baseData = column.getValue();
                            data.put("base_data", baseData.getBytes());
                        }
                        if (StringUtils.equals(name, "guid")) {
                            data.put("guid", Integer.valueOf(column.getValue()));
                        }
                    });
                    return data;
                }).forEach(data->{
                    PlayerDataParser.PlayerInfo playerInfo = parser.parseFromBaseData(data);
                    if(playerInfo != null){
                        playerService.insertPlayerCoupon(playerInfo.getPlayerCouponPo());
                        playerService.insertPlayer(playerInfo.getPlayerPo());
                    }
                });

            }
        }catch (Exception e){
            log.error("handler Db sync error ",e);
        }
    }
}
