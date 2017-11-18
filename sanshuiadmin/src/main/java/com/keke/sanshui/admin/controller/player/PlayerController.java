package com.keke.sanshui.admin.controller.player;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.keke.sanshui.admin.request.order.OrderQueryVo;
import com.keke.sanshui.admin.request.player.PlayerPickRequest;
import com.keke.sanshui.admin.request.player.PlayerQueryVo;
import com.keke.sanshui.admin.response.ApiResponse;
import com.keke.sanshui.admin.response.RetCode;
import com.keke.sanshui.admin.response.order.OrderItemVo;
import com.keke.sanshui.admin.response.player.PlayerPickResponseVo;
import com.keke.sanshui.admin.response.player.PlayerResponseVo;
import com.keke.sanshui.admin.service.order.AdminOrderReadService;
import com.keke.sanshui.admin.service.player.AdminPlayerReadService;
import com.keke.sanshui.base.util.WeekUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/player")
@Slf4j
public class PlayerController {
    @Autowired
    private AdminPlayerReadService adminPlayerReadService;

    @RequestMapping("/list")
    @ResponseBody
    public ApiResponse<List<PlayerResponseVo>> queryList(@RequestBody PlayerQueryVo playerQueryVo){
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

    @RequestMapping("/count")
    @ResponseBody
    public ApiResponse<Long> queryCount(@RequestBody PlayerQueryVo playerQueryVo){
        try{
            Long count =  adminPlayerReadService.queryCount(playerQueryVo);
            return new ApiResponse<>(count);
        }catch (Exception e){
            log.error("queryCount error {}", JSON.toJSONString(playerQueryVo),e);
            return new ApiResponse<>(RetCode.SERVER_ERROR,e.getMessage(), 0L);
        }
    }

    @RequestMapping("/queryPickList")
    @ResponseBody
    public ApiResponse<List<PlayerPickResponseVo>> queryPickList(@RequestBody PlayerPickRequest playerPickRequest){
        try{
            List<PlayerPickResponseVo> playerPickResponseVos =  adminPlayerReadService.queryPickList(playerPickRequest);
            return new ApiResponse<>(playerPickResponseVos);
        }catch (Exception e){
            log.error("queryPickList error {}", JSON.toJSONString(playerPickRequest),e);
            return new ApiResponse<>(RetCode.SERVER_ERROR,e.getMessage(), Lists.newArrayList());
        }
    }
}
