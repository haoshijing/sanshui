package com.keke.sanshui.admin.web;

import com.keke.sanshui.admin.intecptor.AuthInterceptor;
import com.keke.sanshui.admin.intecptor.ProcessInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
@EnableWebMvc
public class WebMvcConf extends WebMvcConfigurerAdapter {

//    @Autowired
//    private CommonInterceptor commonInterceptor;
    @Autowired
    private AuthInterceptor authInterceptor;

    @Autowired
    private ProcessInterceptor processInterceptor;

    /**
     * 请求拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(processInterceptor).addPathPatterns("/**");
        registry.addInterceptor(authInterceptor).addPathPatterns("/**").
                excludePathPatterns("/login");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

    }
}
