package com.keke.sanshui.admin.controller;

import com.keke.sanshui.admin.response.ApiResponse;
import com.keke.sanshui.admin.service.AdminAgentReadService;
import com.keke.sanshui.admin.vo.AgentVo;
import com.keke.sanshui.base.admin.po.AgentPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/agent")
public class AgentController {

    @Autowired
    private AdminAgentReadService adminAgentReadService;
    @RequestMapping("/list")
    @ResponseBody
    public ApiResponse<List<AgentVo>> queryList(){
        return new ApiResponse<>(adminAgentReadService.selectAgentVoList(new AgentPo()));
    }
}
