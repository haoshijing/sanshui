package com.keke.sanshui.syncdata.canal;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:application-syncdata.xml");
        applicationContext.start();
    }
}
