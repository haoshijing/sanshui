package com.keke.sanshui;

import org.eclipse.jetty.util.thread.ShutdownThread;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.CountDownLatch;

public class Launcher {

    public static void main(String[] args)  throws  Exception{
       final CountDownLatch shutDownLatch = new CountDownLatch(1);
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:application-context.xml");
        ctx.start();
        shutDownLatch.await();

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                shutDownLatch.countDown();
            }
        });
    }
}
