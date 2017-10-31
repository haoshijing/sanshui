package com.keke.sanshui.admin.controller;

import com.keke.sanshui.admin.request.LoginDataRequest;
import com.keke.sanshui.admin.response.ApiResponse;
import com.keke.sanshui.admin.response.RetCode;
import com.keke.sanshui.base.admin.service.AdminService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    @Autowired
    private AdminService adminService;
    @RequestMapping("/test")
    @ResponseBody
    public String test(){
        return "test";
    }

    @RequestMapping("/login")
    @ResponseBody
    public ApiResponse<Boolean> login(@RequestBody LoginDataRequest loginDataRequest){
        if(StringUtils.isEmpty(loginDataRequest.getName()) ||
                StringUtils.isEmpty(loginDataRequest.getPassword())){
            return new ApiResponse<>(RetCode.PARAM_ERROR,"参数错误",false);
        }
        Boolean check =   adminService.checkUser(loginDataRequest.getName(),loginDataRequest.getPassword());
        return new ApiResponse<>(check);
    }
}
