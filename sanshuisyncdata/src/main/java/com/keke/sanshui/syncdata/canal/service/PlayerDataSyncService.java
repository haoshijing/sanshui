package com.keke.sanshui.syncdata.canal.service;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.keke.sanshui.base.admin.po.PlayerPo;
import com.keke.sanshui.base.admin.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class PlayerDataSyncService {
    @Autowired
    private PlayerService playerService;
    public void syncPlayerInsertData(List<CanalEntry.RowData> rowDataList){
        PlayerPo playerPo = new PlayerPo();

    }
}
