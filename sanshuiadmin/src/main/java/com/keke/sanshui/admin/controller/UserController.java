package com.keke.sanshui.admin.controller;

import com.google.common.collect.Lists;
import com.keke.sanshui.admin.AbstractController;
import com.keke.sanshui.admin.auth.AdminAuthInfo;
import com.keke.sanshui.admin.request.UpdatePwdRequest;
import com.keke.sanshui.admin.response.ApiResponse;
import com.keke.sanshui.admin.response.RetCode;
import com.keke.sanshui.admin.response.user.UserDataResponse;
import com.keke.sanshui.admin.service.AdminAgentWriteService;
import com.keke.sanshui.base.admin.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController extends AbstractController{

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminAgentWriteService adminAgentWriteService;

    @RequestMapping("/info")
    @ResponseBody
    public ApiResponse<UserDataResponse> getUserInfo(HttpServletRequest request){
        UserDataResponse response = new UserDataResponse();
        AdminAuthInfo adminAuthInfo = getToken(request);
        response.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        if(adminAuthInfo.getLevel() == 1) {
            response.setRoles(Lists.newArrayList("admin"));
        }else if(adminAuthInfo.getLevel() == 2){
            response.setRoles(Lists.newArrayList("areaagent","agent"));
        }else{
            response.setRoles(Lists.newArrayList("agent"));
        }
        response.setIntroduction("");
        response.setName(adminAuthInfo.getUserName());
        return new ApiResponse<>(response);
    }

    @RequestMapping("/updatePwd")
    @ResponseBody
    public ApiResponse<Boolean> updatePwd(@RequestBody UpdatePwdRequest updatePwdRequest,HttpServletRequest request){
        try{
            String oldPwd = updatePwdRequest.getOldPwd();
            String newPwd =updatePwdRequest.getNewPwd();
            if(StringUtils.isEmpty(oldPwd) || StringUtils.isEmpty(newPwd)){
                return new ApiResponse<>(RetCode.PARAM_ERROR,"参数不能为空",false);
            }
            if(StringUtils.equals(oldPwd,newPwd)){
                return new ApiResponse<>(RetCode.PARAM_ERROR,"新旧密码不能一样",false);
            }
            AdminAuthInfo adminAuthInfo = super.getToken(request);
            Integer level = adminAuthInfo.getLevel();
            Boolean updateRet = false ;
            if(level == 1) {
                 updateRet = adminService.updatePwd(oldPwd, newPwd);
            }else{
                updateRet = adminAgentWriteService.updatePwd(oldPwd,newPwd,adminAuthInfo.getUserName());
            }
            if(!updateRet) {
                return new ApiResponse<>(RetCode.SERVER_ERROR,"旧密码错误",false);
            }
            return new ApiResponse<>(updateRet);
        }catch (Exception e){
            return new ApiResponse<>(RetCode.SERVER_ERROR,e.getMessage(),false);
        }
    }

}
