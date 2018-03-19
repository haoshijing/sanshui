package com.keke.sanshui.job.service;

import com.keke.sanshui.base.admin.dao.AgentPickTotalDAO;
import com.keke.sanshui.base.admin.dao.PlayerPickTotalDAO;
import com.keke.sanshui.base.util.WeekUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class CheckTotalService {

    @Autowired
    private PlayerPickTotalDAO playerPickTotalDAO;

    @Autowired
    private AgentPickTotalDAO agentPickTotalDAO;

    @Autowired
    private PlayerTotalService playerTotalService;

    @Autowired
    private AgentTotalService agentTotalService;

    @Scheduled(cron="* * 0/1  * * ? ")
    public void work(){
        try{
            doCheck();
        }catch (Exception e){

        }
    }
    public void doCheck() {
        Integer week = WeekUtil.getCurrentWeek();
        log.info("week = {}", week);

        Long lastWeekNeedCalAgentTotal = playerPickTotalDAO.queryLastWeekNeedCalTotal(week);
        if (lastWeekNeedCalAgentTotal == null) {
            lastWeekNeedCalAgentTotal = 0L;
        }

        Long totalPlayerTotal = playerPickTotalDAO.querySum(week - 1);

        if (totalPlayerTotal == null) {
            totalPlayerTotal = 0L;
        }

        Long totalAgentTotal = agentPickTotalDAO.querySum(week - 1);
        if (totalAgentTotal == null) {
            totalAgentTotal = 0L;
        }

        if (totalPlayerTotal != (totalAgentTotal + lastWeekNeedCalAgentTotal)) {
            //玩家总直冲不等于代理之和 + 需要计算的代理个人充值
            long weekStartTimestamp = WeekUtil.getWeekStartTimestamp(week - 1);
            long weekEndTimestamp = WeekUtil.getWeekEndTimestamp(week - 1);
            playerTotalService.staticPlayerPick(weekStartTimestamp, weekEndTimestamp, week - 1);
            agentTotalService.staticNormalAgent(week - 1);
            agentTotalService.staticAreaAgent(week - 1);
        }

        lastWeekNeedCalAgentTotal = playerPickTotalDAO.queryLastWeekNeedCalTotal(week);
        if (lastWeekNeedCalAgentTotal == null) {
            lastWeekNeedCalAgentTotal = 0L;
        }

        totalPlayerTotal = playerPickTotalDAO.querySum(week - 1);

        if (totalPlayerTotal == null) {
            totalPlayerTotal = 0L;
        }
        totalAgentTotal = agentPickTotalDAO.querySum(week - 1);
        if (totalAgentTotal == null) {
            totalAgentTotal = 0L;
        }
        if (totalPlayerTotal != (totalAgentTotal + lastWeekNeedCalAgentTotal)) {
            //发送告警
            log.error("重新计算后还是不对");
        }
    }
}
