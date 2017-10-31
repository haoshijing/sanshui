package com.keke.sanshui.admin.controller;

import com.keke.sanshui.admin.response.ApiResponse;
import com.keke.sanshui.admin.service.AdminHeadAgentReadService;
import com.keke.sanshui.admin.vo.HeadAgentVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/headagent")
public class HeadAgentController {

    @Autowired
    private AdminHeadAgentReadService adminHeadAgentReadService;
    @RequestMapping("/list")
    public ApiResponse<List<HeadAgentVo>> queryList(){
        return new ApiResponse<>(adminHeadAgentReadService.selectAgentVoList());
    }
}
