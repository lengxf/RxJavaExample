package com.example.chapter02;


import com.example.common.Program;


import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observables.ConnectableObservable;

import static com.example.common.Helpers.subscribePrint;

/**
 * Demonstrates how to create and use ConnectableObservables.
 * 
 * @author meddle
 */
public class UsingConnectableObservables implements Program {

	@Override
	public String name() {
		return "A ConnectableObservable demonstration";
	}

	@Override
	public int chapter() {
		return 3;
	}

	@Override
	public void run() {
		Observable<Long> interval = Observable.interval(100L,
				TimeUnit.MILLISECONDS);
		ConnectableObservable<Long> published = interval.publish();

		Disposable sub1 = subscribePrint(published, "First");
		Disposable sub2 = subscribePrint(published, "Second");

		published.connect();

		Disposable sub3 = null;
		try {
			Thread.sleep(300L);

			sub3 = subscribePrint(published, "Third");
			Thread.sleep(500L);
		} catch (InterruptedException e) {
		}

		sub1.dispose();
		sub2.dispose();
		sub3.dispose();

		System.out.println("-----------------------------------");

		Observable<Long> refCount = interval.share(); // publish().refCount();

		sub1 = subscribePrint(refCount, "First");
		sub2 = subscribePrint(refCount, "Second");

		sub3 = null;
		try {
			Thread.sleep(300L);

			sub3 = subscribePrint(refCount, "Third");
			Thread.sleep(500L);
		} catch (InterruptedException e) {
		}

		sub1.dispose();
		sub2.dispose();
		sub3.dispose();

		Disposable sub4 = subscribePrint(refCount, "Fourth");

		try {
			Thread.sleep(300L);
		} catch (InterruptedException e) {
		}
		sub4.dispose();
	}
	
	public static void main(String[] args) {
		new UsingConnectableObservables().run();
	}
}
