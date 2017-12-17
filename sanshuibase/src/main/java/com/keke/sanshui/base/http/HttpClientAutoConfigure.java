package com.keke.sanshui.base.http;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class HttpClientAutoConfigure {

    @Bean(value = "http",initMethod = "start",destroyMethod = "destroy")
    HttpClient httpClient(){
        SslContextFactory sslContextFactory = new SslContextFactory();
        HttpClient httpClient = new HttpClient(sslContextFactory);
        return httpClient;
    }
}
