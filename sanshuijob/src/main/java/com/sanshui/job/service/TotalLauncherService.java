package com.sanshui.job.service;

import io.netty.util.concurrent.DefaultThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Repository
public class TotalLauncherService {
    @Autowired
    private AgentTotalService agentTotalService;
    @Autowired
    private PlayerTotalService playerTotalService;
    public void init() {

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(new DefaultThreadFactory(""));
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                playerTotalService.work();
                agentTotalService.work();
            }
        }, 1, 60, TimeUnit.MINUTES);
    }
}
