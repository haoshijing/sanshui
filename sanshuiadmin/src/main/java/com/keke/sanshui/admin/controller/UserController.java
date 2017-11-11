package com.keke.sanshui.admin.controller;

import com.keke.sanshui.admin.response.ApiResponse;
import com.keke.sanshui.admin.response.user.UserDataResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    @RequestMapping("/info")
    @ResponseBody
    public ApiResponse<UserDataResponse> getUserInfo(String token, HttpServletRequest request){
        UserDataResponse response = new UserDataResponse();
        String name = (String)request.getSession().getAttribute(token);

        response.setAvatar("xxxxx");
        response.setRole("admin");
        response.setIntroduction("");
        response.setName(name);
        log.info("token = {}",token);
        return new ApiResponse<>(response);
    }

}
