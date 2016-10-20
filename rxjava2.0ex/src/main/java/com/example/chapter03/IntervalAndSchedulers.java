package com.example.chapter03;

import com.example.common.Program;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static com.example.common.Helpers.debug;


/**
 * More information of {@link Observable#interval} and its default {@link Scheduler}.
 * 
 * @author meddle
 */
public class IntervalAndSchedulers implements Program {

	@Override
	public String name() {
		return "Observable.interval and Schedulers";
	}

	@Override
	public int chapter() {
		return 6;
	}

	@Override
	public void run() {
		CountDownLatch latch = new CountDownLatch(1);
		
		Observable.range(5, 5).doOnEach(debug("Test")).subscribe();
		
		Observable
			.interval(500L, TimeUnit.MILLISECONDS)
			.take(5)
			.doOnEach(debug("Default interval"))
			.doAfterTerminate(() -> latch.countDown())
			.subscribe();
		
		try {
			latch.await();
		} catch (InterruptedException e) {}

		Observable
		.interval(500L, TimeUnit.MILLISECONDS, Schedulers.trampoline())
		.take(5)
		.doOnEach(debug("trampoline interval"))
		.subscribe();
	}
	
	public static void main(String[] args) {
		new IntervalAndSchedulers().run();
	}

}
