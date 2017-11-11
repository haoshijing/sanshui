package com.keke.sanshui.admin.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.keke.sanshui.admin.request.AgentRequestVo;
import com.keke.sanshui.admin.request.QueryAgentPo;
import com.keke.sanshui.admin.response.ApiResponse;
import com.keke.sanshui.admin.response.RetCode;
import com.keke.sanshui.admin.service.AdminAgentReadService;
import com.keke.sanshui.admin.service.AdminAgentWriteService;
import com.keke.sanshui.admin.vo.AgentVo;
import com.keke.sanshui.base.admin.po.AgentPo;
import com.keke.sanshui.base.util.WeekUtil;
import com.sun.org.apache.bcel.internal.generic.RET;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/agent")
@Slf4j
public class AgentController{

    @Autowired
    private AdminAgentReadService adminAgentReadService;

    @Autowired
    private AdminAgentWriteService adminAgentWriteService;
    @RequestMapping("/list")
    @ResponseBody
    public ApiResponse<List<AgentVo>> queryList(QueryAgentPo queryAgentPo){
        Integer week = WeekUtil.getCurrentWeek();
        try {
            return new ApiResponse<>(adminAgentReadService.selectAgentVoList(queryAgentPo));
        }catch (Exception e){
            log.error("{}",e);
            return new ApiResponse<>(RetCode.SERVER_ERROR,e.getMessage(), Lists.newArrayList());
        }
    }

    @RequestMapping("/agent/insert")
    @ResponseBody
    public ApiResponse<Boolean> createOrUpdateAgent(@RequestBody AgentRequestVo agentRequestVo){
        Boolean createOrUpdateOk = false;
        Integer adminId = 0;
        try{
            createOrUpdateOk =  adminAgentWriteService.createOrUpdateAgent(agentRequestVo,adminId);
        }catch (Exception e){
            log.error("createOrUpdate agent {} error", JSON.toJSONString(agentRequestVo),e);
        }
        return new ApiResponse<>(createOrUpdateOk);
    }
}
