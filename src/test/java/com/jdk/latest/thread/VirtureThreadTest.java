package com.jdk.latest.thread;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@SpringBootTest
public class VirtureThreadTest {
    private static final Logger log = Logger.getAnonymousLogger();

    @Test
    public void 가상_스레드를_확인한다() throws InterruptedException{
        log.info("\nthreadName: " + Thread.currentThread().getName() + "\navailableProcessors: " + Runtime.getRuntime().availableProcessors());

        final long start = System.currentTimeMillis();
        final AtomicLong index = new AtomicLong();
        final int count = 100;
        final CountDownLatch countDownLatch = new CountDownLatch(count);

        final Runnable runnable = () -> {
            try {
                final long indexValue = index.incrementAndGet();
                Thread.sleep(1000L);

                log.info("\nthreadName: " + Thread.currentThread().getName() + "\nvalue: " + indexValue);
                countDownLatch.countDown();
            } catch (final InterruptedException e) {
                countDownLatch.countDown();
            }
        };

        try (final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < count; i++) {
                executorService.submit(runnable);
            }
        }

        countDownLatch.await();
        final long finish = System.currentTimeMillis();
        final long timeElapsed = finish - start;

        log.info("\nthreadName: " + Thread.currentThread().getName() + "\nRun time: " + timeElapsed);
    }
}
