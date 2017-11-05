package com.keke.sanshui.job;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class JobMain{
    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("application-context.xml");
        ctx.start();
    }
}
