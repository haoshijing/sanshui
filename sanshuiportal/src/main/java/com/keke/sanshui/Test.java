package com.keke.sanshui;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {
    public static void main(String[] args) throws Exception  {
       final HttpClient httpClient = new HttpClient();
        httpClient.start();

        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for(int i = 0; i < 1;i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {

                        try {
                            ContentResponse response = httpClient.newRequest("http://xed006.com/cl/index.php?module=System&method=first").send();
                            System.out.println(response.getContentAsString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                }
            });
        }
    }
}
