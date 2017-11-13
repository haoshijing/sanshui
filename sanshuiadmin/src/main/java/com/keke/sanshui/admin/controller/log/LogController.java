package com.keke.sanshui.admin.controller.log;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.keke.sanshui.admin.request.log.LogQueryVo;
import com.keke.sanshui.admin.request.order.OrderQueryVo;
import com.keke.sanshui.admin.response.ApiResponse;
import com.keke.sanshui.admin.response.RetCode;
import com.keke.sanshui.admin.response.log.LogVo;
import com.keke.sanshui.admin.response.order.OrderItemVo;
import com.keke.sanshui.admin.service.log.AdminLogReadService;
import com.keke.sanshui.admin.service.order.AdminOrderReadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@Slf4j
@RequestMapping("/log")
public class LogController {
    @Autowired
    private AdminLogReadService adminLogReadService;

    @RequestMapping("/list")
    @ResponseBody
    public ApiResponse<List<LogVo>> queryList(@RequestBody LogQueryVo logQueryVo){
        try{
            List<LogVo> list =   adminLogReadService.queryList(logQueryVo);
            return new ApiResponse<>(list);
        }catch (Exception e){
            log.error("queryList error {}", JSON.toJSONString(logQueryVo),e);
            return new ApiResponse<>(RetCode.SERVER_ERROR,e.getMessage(), Lists.newArrayList());
        }
    }

    @RequestMapping("/count")
    @ResponseBody
    public ApiResponse<Long> queryCount(@RequestBody LogQueryVo logQueryVo){
        try{
            Long count =   adminLogReadService.queryCount(logQueryVo);
            return new ApiResponse<>(count);
        }catch (Exception e){
            log.error("queryCount error {}", JSON.toJSONString(logQueryVo),e);
            return new ApiResponse<>(RetCode.SERVER_ERROR,e.getMessage(), 0L);
        }
    }
}
