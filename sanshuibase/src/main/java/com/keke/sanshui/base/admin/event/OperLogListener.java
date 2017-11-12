package com.keke.sanshui.base.admin.event;

import com.keke.sanshui.base.admin.dao.OperLogDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class OperLogListener implements ApplicationListener<OperLogEvent> {

    @Autowired
    private OperLogDAO operLogDAO;
    @Override
    @Async
    public void onApplicationEvent(OperLogEvent event) {
        try{
            int insertRet = operLogDAO.insertLog(event.getOperLogPo());
            log.info("insertRet = {}",insertRet);
        }catch (Exception e){

        }
    }
}
