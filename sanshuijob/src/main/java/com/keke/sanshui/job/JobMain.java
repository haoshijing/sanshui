package com.keke.sanshui.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

@EnableAutoConfiguration
@ImportResource("classpath:application-context.xml")
public class JobMain{
    public static void main(String[] args) {
        SpringApplication.run(JobMain.class, args);
    }
}
