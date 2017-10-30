package com.keke.sanshui.admin.controller;

import com.keke.sanshui.admin.request.LoginDataRequest;
import com.keke.sanshui.base.admin.service.AdminService;
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
    public Boolean login(@RequestBody LoginDataRequest loginDataRequest){
        return  adminService.checkUser(loginDataRequest.getName(),loginDataRequest.getPassword());
    }
}
