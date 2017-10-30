package com.keke.sanshui.admin.controller;

import com.keke.sanshui.base.admin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {

    @Autowired
    private AdminService adminService;
    @RequestMapping("/test")
    @ResponseBody
    public String test(){
        return "test";
    }

    @RequestMapping("/login")
    @ResponseBody
    public Boolean login(String name,String password){
        return  adminService.checkUser(name,password);
    }
}
