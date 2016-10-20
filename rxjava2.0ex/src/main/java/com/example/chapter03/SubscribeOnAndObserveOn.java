package com.example.chapter03;

import com.example.common.Program;

import java.util.concurrent.CountDownLatch;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static com.example.common.Helpers.debug;


/**
 * Demonstrates using subscribeOn and observeOn with {@link Schedulers} and {@link Observable}s.
 *
 * @author meddle
 */
public class SubscribeOnAndObserveOn implements Program {

	@Override
	public String name() {
		return "A few examples of observeOn and subscribeOn";
	}

	@Override
	public int chapter() {
		return 6;
	}

	@Override
	public void run() {
		CountDownLatch latch = new CountDownLatch(1);

		Observable<Integer> range = Observable
				.range(20, 5)
				.flatMap(n -> Observable
						.range(n, 3)
						.subscribeOn(Schedulers.computation())
						.doOnEach(debug("Source"))
						);
		
		
		Observable<Character> chars = range
				.observeOn(Schedulers.newThread())
				.map(n -> n + 48)
				.doOnEach(debug("+48 ", "    "))
				.observeOn(Schedulers.computation())
				.map(n -> Character.toChars(n))
				.map(c -> c[0])
				.doOnEach(debug("Chars ", "    "))
				.doAfterTerminate(() -> latch.countDown());
		
		chars.subscribe();

		System.out.println("Hey!");
		
		try {
			latch.await();
		} catch (InterruptedException e) {}
	}

	public static void main(String[] args) {
		new SubscribeOnAndObserveOn().run();
	}
}
