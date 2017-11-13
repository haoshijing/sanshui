package com.keke.sanshui.admin.controller.order;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.keke.sanshui.admin.request.order.OrderQueryVo;
import com.keke.sanshui.admin.response.ApiResponse;
import com.keke.sanshui.admin.response.RetCode;
import com.keke.sanshui.admin.response.order.OrderItemVo;
import com.keke.sanshui.admin.service.order.AdminOrderReadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private AdminOrderReadService adminOrderReadService;

    @RequestMapping("/list")
    @ResponseBody
    public ApiResponse<List<OrderItemVo>> queryList(@RequestBody OrderQueryVo orderQueryVo){
        try{
            List<OrderItemVo> list =   adminOrderReadService.queryList(orderQueryVo);
            return new ApiResponse<>(list);
        }catch (Exception e){
            log.error("queryList error {}", JSON.toJSONString(orderQueryVo),e);
            return new ApiResponse<>(RetCode.SERVER_ERROR,e.getMessage(), Lists.newArrayList());
        }
    }

    @RequestMapping("/count")
    @ResponseBody
    public ApiResponse<Long> queryCount(@RequestBody OrderQueryVo orderQueryVo){
        try{
            Long count =  adminOrderReadService.queryCount(orderQueryVo);
            return new ApiResponse<>(count);
        }catch (Exception e){
            log.error("queryCount error {}", JSON.toJSONString(orderQueryVo),e);
            return new ApiResponse<>(RetCode.SERVER_ERROR,e.getMessage(), 0L);
        }
    }
}
