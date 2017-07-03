package com.sftc.tools.timer;

import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TimerTest {

    public static void main(String[] args) throws InterruptedException {

        final TimerTask task = new TimerTask() {

            @Override
            public void run() {
                System.out.println(System.currentTimeMillis());
            }
        };

        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
        pool.scheduleAtFixedRate(task, 0, 1000, TimeUnit.MILLISECONDS);
    }
}