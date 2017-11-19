package com.keke.sanshui.admin.controller;

import com.keke.sanshui.admin.AbstractController;
import com.keke.sanshui.admin.auth.AdminAuthCacheService;
import com.keke.sanshui.admin.auth.AdminAuthInfo;
import com.keke.sanshui.admin.request.LoginDataRequest;
import com.keke.sanshui.admin.response.ApiResponse;
import com.keke.sanshui.admin.response.RetCode;
import com.keke.sanshui.admin.response.impl.LoginResponse;
import com.keke.sanshui.admin.service.AdminAgentReadService;
import com.keke.sanshui.base.admin.service.AdminService;
import com.keke.sanshui.base.admin.service.AgentService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class LoginController extends AbstractController{

    @Autowired
    private AdminService adminService;


    @Autowired
    private AdminAgentReadService adminAgentReadService;

    @RequestMapping("/login")
    @ResponseBody
    public ApiResponse<LoginResponse> login(@RequestBody LoginDataRequest loginDataRequest, HttpServletRequest request){
        LoginResponse loginResponse = new LoginResponse();

        if(StringUtils.isEmpty(loginDataRequest.getName()) ||
                StringUtils.isEmpty(loginDataRequest.getPassword())){
            return new ApiResponse<>(RetCode.PARAM_ERROR,"参数错误",loginResponse);
        }
        String clientIp = request.getRemoteAddr();
        Boolean isAdmin = StringUtils.equals(loginDataRequest.getName(),"superadmin");
        Boolean check;
        AdminAuthInfo adminAuthInfo = new AdminAuthInfo();
        if(!isAdmin){
           Pair<Boolean,Integer> pair =   adminAgentReadService.checkUser(loginDataRequest.getName(),loginDataRequest.getPassword());
           if(pair.getLeft()){
               adminAuthInfo.setLevel(pair.getRight());
           }
           check = pair.getLeft();
        }else{
            check =  adminService.checkUser(loginDataRequest.getName(),loginDataRequest.getPassword(),clientIp);
            adminAuthInfo.setLevel(1);
        }
        loginResponse.setSucc(check);
        if(check){
            String token = UUID.randomUUID().toString().replace("-","");
            loginResponse.setToken(token);
            adminAuthInfo.setToken(token);
            adminAuthInfo.setUserName(loginDataRequest.getName());
            adminAuthCacheService.setTokenCache(token,adminAuthInfo);
        }
        return new ApiResponse<>(loginResponse);
    }

    @RequestMapping("/logout")
    @ResponseBody
    public ApiResponse<Boolean> login(HttpServletRequest request){
        AdminAuthInfo adminAuthInfo = getToken(request);
        if(adminAuthInfo.getLevel() == 1) {
            adminService.logout();
        }
        adminAuthCacheService.deleteToken(adminAuthInfo.getToken());
        return new ApiResponse<>(true);
    }
}
