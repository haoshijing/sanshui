package com.keke.sanshui.base.http;

import org.eclipse.jetty.client.HttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class HttpClientAutoConfigure {

    @Bean(value = "http",initMethod = "start",destroyMethod = "destroy")
    HttpClient httpClient(){
        HttpClient httpClient = new HttpClient();
        return httpClient;
    }
}
