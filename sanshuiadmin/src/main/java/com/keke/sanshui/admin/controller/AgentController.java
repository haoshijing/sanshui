package com.keke.sanshui.admin.controller;

import com.alibaba.fastjson.JSON;
import com.keke.sanshui.admin.request.AgentRequestVo;
import com.keke.sanshui.admin.request.QueryAgentPo;
import com.keke.sanshui.admin.response.ApiResponse;
import com.keke.sanshui.admin.service.AdminAgentReadService;
import com.keke.sanshui.admin.service.AdminAgentWriteService;
import com.keke.sanshui.admin.vo.AgentVo;
import com.keke.sanshui.base.admin.po.AgentPo;
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
        return new ApiResponse<>(adminAgentReadService.selectAgentVoList(queryAgentPo));
    }

    @RequestMapping("/agent/insert")
    @ResponseBody
    public ApiResponse<Boolean> createOrUpdateAgent(@RequestBody AgentRequestVo agentRequestVo){
        Boolean createOrUpdateOk = false;
        Integer adminId = 0;
        try{
            createOrUpdateOk =  adminAgentReadService.createOrUpdateAgent(agentRequestVo,adminId);
        }catch (Exception e){
            log.error("createOrUpdate agent {} error", JSON.toJSONString(agentRequestVo),e);
        }
        return new ApiResponse<>(createOrUpdateOk);
    }
}
