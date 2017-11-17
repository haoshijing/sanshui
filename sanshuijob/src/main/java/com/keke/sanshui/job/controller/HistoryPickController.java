package com.keke.sanshui.job.controller;

import com.keke.sanshui.base.admin.dao.PlayerPickTotalDAO;
import com.keke.sanshui.base.admin.po.AgentPickTotalPo;
import com.keke.sanshui.base.admin.po.PlayerPickTotalPo;
import com.keke.sanshui.base.admin.po.PlayerPo;
import com.keke.sanshui.base.admin.po.agent.AgentPo;
import com.keke.sanshui.base.admin.po.agent.AgentQueryPo;
import com.keke.sanshui.base.admin.service.OrderService;
import com.keke.sanshui.base.admin.service.PlayerService;
import com.keke.sanshui.base.util.WeekUtil;
import com.keke.sanshui.job.service.AgentTotalService;
import com.keke.sanshui.job.service.PlayerTotalService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/history")
@Controller
@Slf4j
public class HistoryPickController {

    @Autowired
    private PlayerTotalService playerTotalService;
    @Autowired
    private AgentTotalService agentTotalService;

    @RequestMapping("/beginToPlayer")
    @ResponseBody
    public String beginToAcc(int startWeek,int endWeek){
        for(int i = startWeek ; i < endWeek;i++ ){
            long weekStartTimestamp = WeekUtil.getWeekStartTimestamp(i);
            long weekEndTimestamp = WeekUtil.getWeekEndTimestamp(i);
            playerTotalService.staticPlayerPick(weekStartTimestamp,weekEndTimestamp,i);
        }
        return "OK";
    }

    @RequestMapping("/beginToAgent")
    @ResponseBody
    public String beginToAccAgent(int startWeek,int endWeek){

        for(int i = startWeek ; i < endWeek;i++ ){
            agentTotalService.staticNormalAgent(i);
            agentTotalService.staticAreaAgent(i);
        }
        return "OK";
    }
}
