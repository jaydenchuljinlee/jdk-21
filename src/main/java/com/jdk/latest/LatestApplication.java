package com.jdk.latest;

import org.slf4j.MDC;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

// @SpringBootApplication
public class LatestApplication {
	private static final Logger log = Logger.getAnonymousLogger();

	public void main(String[] args) throws InterruptedException {


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


		try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
			Future<String> str = scope.fork(this::toString);
			Future<List<String>> list = scope.fork(this::list);

			scope.join();

		}
		// SpringApplication.run(LatestApplication.class, args);
	}

	public static String str() {
		return "This is String";
	}

	public static List<String> list() {
		List<String> result = new ArrayList<>();
		result.add("This is list");
		return result;
	}
}
