package com.keke.sanshui.admin.agent.controller;

import com.keke.sanshui.admin.AbstractController;
import com.keke.sanshui.admin.auth.AdminAuthInfo;
import com.keke.sanshui.admin.request.order.OrderQueryVo;
import com.keke.sanshui.admin.response.ApiResponse;
import com.keke.sanshui.admin.response.order.OrderItemVo;
import com.keke.sanshui.admin.service.order.AdminOrderReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/agentbiz/order")
@Controller
public class AgentOrderController extends AbstractController {

    @Autowired
    private AdminOrderReadService adminOrderReadService;
    @RequestMapping("/agentorderlist")
    @ResponseBody
    public ApiResponse<List<OrderItemVo>> queryList(@RequestBody OrderQueryVo orderQueryVo, HttpServletRequest request){
        AdminAuthInfo adminAuthInfo = getToken(request);
        orderQueryVo.setGuid(adminAuthInfo.getUserName());
        return new ApiResponse<>(adminOrderReadService.queryList(orderQueryVo));
    }

    @RequestMapping("/agentordercount")
    @ResponseBody
    public ApiResponse<Long> queryCount(@RequestBody OrderQueryVo orderQueryVo, HttpServletRequest request){
        AdminAuthInfo adminAuthInfo = getToken(request);
        orderQueryVo.setGuid(adminAuthInfo.getUserName());
        return new ApiResponse<>(adminOrderReadService.queryCount(orderQueryVo));
    }
}
