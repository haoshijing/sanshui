package com.keke.sanshui.syncdata.canal.listener;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.keke.sanshui.syncdata.canal.event.DeleteCanalEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Repository;

//@Repository
@Slf4j
public class DeleteEventListener implements ApplicationListener<DeleteCanalEvent>{
    @Override
    public void onApplicationEvent(DeleteCanalEvent event) {
        CanalEntry.Entry entry = event.getEntry();
        try {
            String tableName = entry.getHeader().getTableName();
            if (!StringUtils.equals(tableName, "characters")) {
                return;
            }

        }catch (Exception e){

        }
    }
}
