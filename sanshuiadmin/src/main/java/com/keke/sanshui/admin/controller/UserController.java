package com.keke.sanshui.admin.controller;

import com.keke.sanshui.admin.request.UpdatePwdRequest;
import com.keke.sanshui.admin.response.ApiResponse;
import com.keke.sanshui.admin.response.RetCode;
import com.keke.sanshui.admin.response.user.UserDataResponse;
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
public class UserController {

    @Autowired
    private AdminService adminService;
    @RequestMapping("/info")
    @ResponseBody
    public ApiResponse<UserDataResponse> getUserInfo(String token, HttpServletRequest request){
        UserDataResponse response = new UserDataResponse();
        String name = (String)request.getSession().getAttribute(token);
        response.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        response.setRole("admin");
        response.setIntroduction("");
        response.setName(name);
        log.info("token = {}",token);
        return new ApiResponse<>(response);
    }

    @RequestMapping("/updatePwd")
    @ResponseBody
    public ApiResponse<Boolean> updatePwd(@RequestBody UpdatePwdRequest updatePwdRequest){
        try{
            String oldPwd = updatePwdRequest.getOldPwd();
            String newPwd =updatePwdRequest.getNewPwd();
            if(StringUtils.isEmpty(oldPwd) || StringUtils.isEmpty(newPwd)){
                return new ApiResponse<>(RetCode.PARAM_ERROR,"参数不能为空",false);
            }
            if(StringUtils.equals(oldPwd,newPwd)){
                return new ApiResponse<>(RetCode.PARAM_ERROR,"新旧密码不能一样",false);
            }
            Boolean updateRet  = adminService.updatePwd(oldPwd, newPwd);
            if(!updateRet) {
                return new ApiResponse<>(RetCode.SERVER_ERROR,"旧密码错误",false);
            }
            return new ApiResponse<>(updateRet);
        }catch (Exception e){
            return new ApiResponse<>(RetCode.SERVER_ERROR,e.getMessage(),false);
        }
    }

}
