package com.keke.sanshui.syncdata.canal;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.keke.sanshui.syncdata.canal.event.DeleteCanalEvent;
import com.keke.sanshui.syncdata.canal.event.InsertCanalEvent;
import com.keke.sanshui.syncdata.canal.event.UpdateCanalEvent;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.*;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//@Repository
public class CanalScheduling implements  ApplicationContextAware {
    private static final Logger logger = LoggerFactory.getLogger(CanalScheduling.class);
    private ApplicationContext applicationContext;

    @Resource
    private CanalConnector canalConnector;

    private ScheduledExecutorService executorService;
    private volatile  boolean isRun =false;

    @PostConstruct
    public void init(){
        executorService = Executors.newScheduledThreadPool(1,new DefaultThreadFactory("CanalSyncDataThread"));
    }

    @EventListener
    public void handlerContextInit(ContextRefreshedEvent contextRefreshedEvent){
        isRun = true;
    }
    @EventListener
    public void handlerContextInit(ContextStartedEvent event){
        start();
    }


    public void start(){
        if(executorService != null){
            executorService.scheduleWithFixedDelay(new Runnable() {
                @Override
                public void run() {
                    if(isRun) {
                        doSync();
                    }
                }
            },100,100, TimeUnit.MILLISECONDS);
        }
    }

    public void doSync() {
        try {
            int batchSize = 1000;
//            Message message = connector.get(batchSize);
            Message message = canalConnector.getWithoutAck(batchSize);
            long batchId = message.getId();
            logger.debug("scheduled_batchId=" + batchId);
            try {
                List<CanalEntry.Entry> entries = message.getEntries();
                if (batchId != -1 && entries.size() > 0) {
                    entries.forEach(entry -> {
                        if (entry.getEntryType() == CanalEntry.EntryType.ROWDATA) {
                            publishCanalEvent(entry);
                        }
                    });
                }
                canalConnector.ack(batchId);
            } catch (Exception e) {
                logger.error("发送监听事件失败！batchId回滚,batchId=" + batchId, e);
                canalConnector.rollback(batchId);
            }
        } catch (Exception e) {
            logger.error("canal_scheduled异常！", e);
        }
    }

    private void publishCanalEvent(CanalEntry.Entry entry) {
        CanalEntry.EventType eventType = entry.getHeader().getEventType();
        switch (eventType) {
            case INSERT:
                applicationContext.publishEvent(new InsertCanalEvent(entry));
                break;
            case UPDATE:
                applicationContext.publishEvent(new UpdateCanalEvent(entry));
                break;
            case DELETE:
                applicationContext.publishEvent(new DeleteCanalEvent(entry));
                break;
            default:
                break;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
