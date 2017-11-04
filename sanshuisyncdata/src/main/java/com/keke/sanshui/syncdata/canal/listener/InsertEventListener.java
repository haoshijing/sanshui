package com.keke.sanshui.syncdata.canal.listener;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.keke.sanshui.syncdata.canal.event.InsertCanalEvent;
import com.keke.sanshui.syncdata.canal.service.PlayerDataSyncService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Repository;


@Slf4j
@Repository
public class InsertEventListener implements ApplicationListener<InsertCanalEvent> {

    @Autowired
    PlayerDataSyncService playerDataSyncService;

    @Override
    public void onApplicationEvent(InsertCanalEvent event) {
        CanalEntry.Entry entry = event.getEntry();
        String tableName = entry.getHeader().getTableName();
        try {
            CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            if (StringUtils.equals("t_player", tableName)) {
                playerDataSyncService.syncPlayerInsertData(rowChange.getRowDatasList());
            }
        }catch (Exception e){
            log.error("handler Db sync error ",e);
        }
    }
}
