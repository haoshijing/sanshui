package com.keke.sanshui.syncdata.canal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ImportResource;


@EnableAutoConfiguration
@ImportResource({"classpath:application-context.xml"})
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
