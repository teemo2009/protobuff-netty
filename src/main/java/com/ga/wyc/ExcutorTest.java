package com.ga.wyc;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ExcutorTest {
    static AtomicInteger count=new AtomicInteger(0);
    public static void main(String[] args) throws InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(2);
        for (int i = 0; i < 6; i++) {
            final int index = i;
            System.out.println("task: " + (i+1));
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    System.out.println("thread start" + index);
                    try {
                        Thread.sleep(Long.MAX_VALUE);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("thread end" + index);
                }
            };
            service.execute(run);
        }
    }


    public static void test() throws InterruptedException {
        Thread.sleep(2000);
        count.incrementAndGet();
    }
}
