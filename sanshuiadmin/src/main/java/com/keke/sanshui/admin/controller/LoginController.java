package com.keke.sanshui.admin.controller;

import com.keke.sanshui.admin.request.LoginDataRequest;
import com.keke.sanshui.admin.response.ApiResponse;
import com.keke.sanshui.admin.response.RetCode;
import com.keke.sanshui.admin.response.impl.LoginResponse;
import com.keke.sanshui.base.admin.service.AdminService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class LoginController {

    @Autowired
    private AdminService adminService;

    @RequestMapping("/login")
    @ResponseBody
    public ApiResponse<LoginResponse> login(@RequestBody LoginDataRequest loginDataRequest, HttpServletRequest request){
        LoginResponse loginResponse = new LoginResponse();

        if(StringUtils.isEmpty(loginDataRequest.getName()) ||
                StringUtils.isEmpty(loginDataRequest.getPassword())){
            return new ApiResponse<>(RetCode.PARAM_ERROR,"参数错误",loginResponse);
        }
        Boolean check =   adminService.checkUser(loginDataRequest.getName(),loginDataRequest.getPassword());
        loginResponse.setSucc(check);
        if(check) {
            String token = UUID.randomUUID().toString().replace("-","");
            request.getSession().setAttribute(token,loginDataRequest.getName());
            loginResponse.setToken(token);
        }
        return new ApiResponse<>(loginResponse);
    }

    @RequestMapping("/logout")
    @ResponseBody
    public ApiResponse<Boolean> login(String token,HttpServletRequest request){
        request.getSession().removeAttribute(token);
        return new ApiResponse<>(true);
    }
}
