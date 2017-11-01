package com.sanshui.job;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main{
    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("application-context.xml");
        ctx.start();
    }
}
