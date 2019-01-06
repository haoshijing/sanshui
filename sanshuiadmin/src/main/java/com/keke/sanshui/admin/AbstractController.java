package com.keke.sanshui.admin;

import com.keke.sanshui.admin.auth.AdminAuthCacheService;
import com.keke.sanshui.admin.auth.AdminAuthInfo;
import org.eclipse.jetty.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;

public class AbstractController {

    @Autowired
    protected HttpClient httpClient;

    @Value("${queryServerHost}")
    protected String queryServerHost;

    @Autowired
    protected AdminAuthCacheService adminAuthCacheService;
    private static  final String TOKEN = "X-TOKEN";
    public AdminAuthInfo getToken(HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader(TOKEN);
        return adminAuthCacheService.getByToken(token);
    }
}
