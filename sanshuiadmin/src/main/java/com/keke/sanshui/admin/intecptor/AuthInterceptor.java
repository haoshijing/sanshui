package com.keke.sanshui.admin.intecptor;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.keke.sanshui.admin.auth.AdminAuthCacheService;
import com.keke.sanshui.admin.auth.AdminAuthInfo;
import com.keke.sanshui.admin.response.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

public class AuthInterceptor extends HandlerInterceptorAdapter {
    private static  final String TOKEN = "X-TOKEN";

    @Autowired
    private AdminAuthCacheService adminAuthCacheService;

    private List<String> whiteUrlList = Lists.newArrayList("/login");
    /**
     * This implementation always returns {@code true}.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String token = request.getHeader(TOKEN);
        boolean preLogin = true;
        if(StringUtils.isEmpty(token)){
            preLogin = isPassUrl(request);
        }else {
            AdminAuthInfo adminAuthInfo = adminAuthCacheService.getByToken(token);
            if (adminAuthInfo == null) {
                preLogin = false;
            }
        }
        if(!preLogin){
            response.setContentType("application/json");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("code",-1);
            jsonObject.put("msg","你的登录已失效");
            response.getWriter().print(jsonObject);
        }
        return preLogin;
    }

    private boolean isPassUrl(HttpServletRequest request){
        String requestUri = request.getRequestURI();
        for(String writeUrl : whiteUrlList){
            if(requestUri.contains(writeUrl)){
                return  true;
            }
        }
        return false;
    }

}
