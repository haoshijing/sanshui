package com.keke.sanshui.admin.controller;

import com.google.common.collect.Lists;
import com.keke.sanshui.admin.response.ApiResponse;
import com.keke.sanshui.admin.response.RetCode;
import com.keke.sanshui.admin.response.index.PickDataResponse;
import com.keke.sanshui.admin.response.index.PickLastWeekData;
import com.keke.sanshui.admin.service.IndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("/index")
@Controller
@Slf4j
public class IndexController {

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
    public ApiResponse<List<PickLastWeekData>> getLastWeekPick(){
        try{
            List<PickLastWeekData> responses = indexService.getLast7DayPick();
            return new ApiResponse<>(responses);
        }catch (Exception e){
            log.error("{}",e);
            return new ApiResponse<>(RetCode.SERVER_ERROR,"获取失败", Lists.newArrayList());
        }
    }
}
