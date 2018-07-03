package com.keke.sanshui;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {
    public static void main(String[] args) throws Exception  {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:application-web.xml");
        context.start();
    }
}
