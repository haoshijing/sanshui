package com.keke.sanshui.syncdata.canal.listener;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.keke.sanshui.syncdata.canal.event.UpdateCanalEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class UpdateEventListener implements ApplicationListener<UpdateCanalEvent> {
    @Override
    public void onApplicationEvent(UpdateCanalEvent event) {
        CanalEntry.Entry entry = event.getEntry();
        String tableName = entry.getHeader().getTableName();
        try {
            CanalEntry.RowChange rowChange =  CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            if(rowChange != null){
                List<CanalEntry.RowData> rowDataList =   rowChange.getRowDatasList();
                rowDataList.forEach(rowData -> {
                    for(int i = 0; i < rowData.getAfterColumnsCount();i++){
                        log.info("after = {}",rowData.getAfterColumns(i));
                        log.info("before = {}",rowData.getBeforeColumns(i));
                    }
                });
            }
        }catch (Exception e){

        }
    }
}
