package com.keke.sanshui.admin.controller;

import com.google.common.collect.Lists;
import com.keke.sanshui.admin.AbstractController;
import com.keke.sanshui.admin.auth.AdminAuthInfo;
import com.keke.sanshui.admin.response.ApiResponse;
import com.keke.sanshui.admin.response.RetCode;
import com.keke.sanshui.admin.response.agent.AgentIndexVo;
import com.keke.sanshui.admin.response.index.PickDataResponse;
import com.keke.sanshui.admin.response.index.PickLastWeekData;
import com.keke.sanshui.admin.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/index")
@Controller
@Slf4j
public class IndexController extends AbstractController{

    @Autowired
    private IndexService indexService;

    @RequestMapping("/currentDayTotal")
    @ResponseBody
    public ApiResponse<PickDataResponse> currentDayTotal(){
        PickDataResponse pickDataResponse = new PickDataResponse();
        pickDataResponse.setDayPickTotal(0L);
        pickDataResponse.setDaySuccessTotal(0L);
        try{
            pickDataResponse = indexService.getCurrentDayPick();
            return new ApiResponse<>(pickDataResponse);
        }catch (Exception e){
            log.error("{}",e);
            return new ApiResponse<>(RetCode.SERVER_ERROR,"获取失败",pickDataResponse);
        }
    }

    @RequestMapping("/getLastWeekPick")
    @ResponseBody
    public ApiResponse<List<PickLastWeekData>> getLastWeekPick(String  days){
        try{
            int day = 7;
            if(StringUtils.isNotEmpty(days)){
                day = Integer.valueOf(days);
            }
            List<PickLastWeekData> responses = indexService.getLastPick(day);
            return new ApiResponse<>(responses);
        }catch (Exception e){
            log.error("days {}",e);
            return new ApiResponse<>(RetCode.SERVER_ERROR,"获取失败", Lists.newArrayList());
        }
    }

    @RequestMapping("/currentAgent")
    @ResponseBody
    public ApiResponse<AgentIndexVo> obtainCurrentAgentInfo(HttpServletRequest request){
        AdminAuthInfo adminAuthInfo = getToken(request);
        AgentIndexVo agentIndexVo = indexService.obtainCurrentAgentInfo(Integer.valueOf(adminAuthInfo.getUserName()));
        return new ApiResponse<>(agentIndexVo);
    }
}


