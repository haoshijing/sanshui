package com.keke.sanshui.admin;

import com.keke.sanshui.admin.auth.AdminAuthCacheService;
import com.keke.sanshui.admin.auth.AdminAuthInfo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class AbstractController {
    @Autowired
    protected AdminAuthCacheService adminAuthCacheService;
    private static  final String TOKEN = "X-TOKEN";
    public AdminAuthInfo getToken(HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader(TOKEN);
        return adminAuthCacheService.getByToken(token);
    }
}
