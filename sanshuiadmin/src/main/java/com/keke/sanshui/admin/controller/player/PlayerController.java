package com.keke.sanshui.admin.controller.player;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.keke.sanshui.admin.AbstractController;
import com.keke.sanshui.admin.auth.AdminAuthInfo;
import com.keke.sanshui.admin.request.order.OrderQueryVo;
import com.keke.sanshui.admin.request.player.PlayerPickRequest;
import com.keke.sanshui.admin.request.player.PlayerQueryVo;
import com.keke.sanshui.admin.response.ApiResponse;
import com.keke.sanshui.admin.response.RetCode;
import com.keke.sanshui.admin.response.order.OrderItemVo;
import com.keke.sanshui.admin.response.player.AgentPlayerResponseVo;
import com.keke.sanshui.admin.response.player.PlayerPickResponseVo;
import com.keke.sanshui.admin.response.player.PlayerResponseVo;
import com.keke.sanshui.admin.service.AdminAgentReadService;
import com.keke.sanshui.admin.service.order.AdminOrderReadService;
import com.keke.sanshui.admin.service.player.AdminPlayerReadService;
import com.keke.sanshui.base.util.WeekUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/player")
@Slf4j
public class PlayerController extends AbstractController {
    @Autowired
    private AdminPlayerReadService adminPlayerReadService;

    @Autowired
    private AdminAgentReadService adminAgentReadService;

    @RequestMapping("/list")
    @ResponseBody
    public ApiResponse<List<PlayerResponseVo>> queryList(@RequestBody PlayerQueryVo playerQueryVo, HttpServletRequest request){
        try{
            if(playerQueryVo.getWeek() == null){
                playerQueryVo.setWeek(WeekUtil.getCurrentWeek());
            }
            List<PlayerResponseVo> list =   adminPlayerReadService.queryList(playerQueryVo);
            return new ApiResponse<>(list);
        }catch (Exception e){
            log.error("queryList error {}", JSON.toJSONString(playerQueryVo),e);
            return new ApiResponse<>(RetCode.SERVER_ERROR,e.getMessage(), Lists.newArrayList());
        }
    }

}
