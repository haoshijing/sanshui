package com.keke.sanshui.admin.agent.controller;

import com.keke.sanshui.admin.AbstractController;
import com.keke.sanshui.admin.agent.response.UnderAgentResponseVo;
import com.keke.sanshui.admin.request.player.PlayerQueryVo;
import com.keke.sanshui.admin.response.ApiResponse;
import com.keke.sanshui.admin.response.RetCode;
import com.keke.sanshui.admin.service.AdminAgentReadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
@RequestMapping("/agentbiz/")
public class AgentController extends AbstractController{
    @Autowired
    private AdminAgentReadService adminAgentReadService;

    @RequestMapping("/queryUnderAgentList")
    public ApiResponse<UnderAgentResponseVo> queryUnderAgentList(HttpServletRequest request,@RequestBody PlayerQueryVo playerQueryVo){
        try{
            Integer areaAgentGuid = Integer.parseInt(getToken(request).getUserName());
            UnderAgentResponseVo underAgentResponseVo = adminAgentReadService.queryUnderAgentList(areaAgentGuid,playerQueryVo);
            return new ApiResponse<>(underAgentResponseVo);
        }catch (Exception e){
            log.error("",e);
            return new ApiResponse<>(RetCode.SERVER_ERROR,e.getMessage(),null);
        }
    }

    @RequestMapping("/queryUnderAgentCount")
    public ApiResponse<Long> queryUnderAgentCount(HttpServletRequest request,@RequestBody PlayerQueryVo playerQueryVo){
        try{
            Integer areaAgentGuid = Integer.parseInt(getToken(request).getUserName());
            Long count = adminAgentReadService.queryUnderAgentCount(areaAgentGuid,playerQueryVo);
            return new ApiResponse<>(count);
        }catch (Exception e){
            log.error("",e);
            return new ApiResponse<>(RetCode.SERVER_ERROR,e.getMessage(),0L);
        }
    }
}
