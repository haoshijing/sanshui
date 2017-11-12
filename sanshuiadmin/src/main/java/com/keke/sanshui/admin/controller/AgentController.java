package com.keke.sanshui.admin.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.keke.sanshui.admin.request.AgentRequestVo;
import com.keke.sanshui.admin.request.agent.AgentQueryVo;
import com.keke.sanshui.admin.response.ApiResponse;
import com.keke.sanshui.admin.response.RetCode;
import com.keke.sanshui.admin.response.agent.AreaAgentVo;
import com.keke.sanshui.admin.service.AdminAgentReadService;
import com.keke.sanshui.admin.service.AdminAgentWriteService;
import com.keke.sanshui.admin.vo.AgentVo;
import com.keke.sanshui.base.util.WeekUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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
    public ApiResponse<List<AgentVo>> queryList(@RequestBody AgentQueryVo agentQueryVo){
        Integer week = WeekUtil.getCurrentWeek();
        try {
            return new ApiResponse<>(adminAgentReadService.selectAgentVoList(agentQueryVo));
        }catch (Exception e){
            log.error("{}",e);
            return new ApiResponse<>(RetCode.SERVER_ERROR,e.getMessage(), Lists.newArrayList());
        }
    }

    @RequestMapping("/resetPwd")
    @ResponseBody
    public ApiResponse<Boolean> resetPwd(Integer agentId){
        Integer week = WeekUtil.getCurrentWeek();
        try {
            return new ApiResponse<>(adminAgentWriteService.resetPwd(agentId));
        }catch (Exception e){
            log.error("{}",e);
            return new ApiResponse<>(RetCode.SERVER_ERROR,e.getMessage(),false);
        }
    }

    @RequestMapping("/count")
    @ResponseBody
    public ApiResponse<Long> queryCount(@RequestBody AgentQueryVo agentQueryVo){
        Integer week = WeekUtil.getCurrentWeek();
        try {
            return new ApiResponse<>(adminAgentReadService.selectCount(agentQueryVo));
        }catch (Exception e){
            log.error("{}",e);
            return new ApiResponse<>(RetCode.SERVER_ERROR,e.getMessage(),0L);
        }
    }

    @RequestMapping("/obtainChooseAgentList")
    @ResponseBody
    public ApiResponse<List<Integer>> getCanChooseAgentList(){
        try{
            List<Integer> agentIds =  adminAgentWriteService.getCanChooseAgentList();
            return new ApiResponse<>(agentIds);
        }catch (Exception e){
            log.error("getCanChooseAgentList error",e);
        }
        return new ApiResponse<>(RetCode.SERVER_ERROR,"获得失败",Lists.newArrayList());
    }

    @RequestMapping("/obtainChooseAreaAgentList")
    @ResponseBody
    public ApiResponse<List<AreaAgentVo>> getCanChooseAreaAgentList(HttpServletRequest request){
        try{
            Integer currentAgentId = null;
            if(request.getParameter("currentAgentId") != null){
                currentAgentId = Integer.valueOf(request.getParameter("currentAgentId"));
            }
            List<AreaAgentVo> agentIds =  adminAgentWriteService.getCanChooseAreaAgent(currentAgentId);
            return new ApiResponse<>(agentIds);
        }catch (Exception e){
            log.error("getCanChooseAgentList error",e);
        }
        return new ApiResponse<>(RetCode.SERVER_ERROR,"获得失败",Lists.newArrayList());
    }

    @RequestMapping("/createUpdate")
    @ResponseBody
    public ApiResponse<Boolean> createOrUpdateAgent(@RequestBody AgentRequestVo agentRequestVo){
        try{
            Pair<Boolean,String> pair =  adminAgentWriteService.createOrUpdateAgent(agentRequestVo);
            return new ApiResponse<>(RetCode.OK,pair.getRight(),pair.getLeft());
        }catch (Exception e){
            log.error("createOrUpdate agent {} error", JSON.toJSONString(agentRequestVo),e);
        }
        return new ApiResponse<>(RetCode.SERVER_ERROR,"设置失败",false);
    }
}
