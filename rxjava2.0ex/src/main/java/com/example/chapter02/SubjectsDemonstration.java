package com.example.chapter02;


import com.example.common.Program;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import static com.example.common.Helpers.subscribePrint;


/**
 * Demonstration of using Subjects and what we could do with them.
 * Uses a {@link PublishSubject} to subscribe to an {@link Observable} and propagate its notifications.
 * 
 * @author meddle
 */
public class SubjectsDemonstration implements Program {

	@Override
	public String name() {
		return "Subjects demonstration";
	}

	@Override
	public int chapter() {
		return 3;
	}



	@Override
	public void run() {

		Observable<Long> interval = Observable.interval(100L,
				TimeUnit.MILLISECONDS);

		Subject<Long> publishSubject = PublishSubject.create();
		interval.subscribe(publishSubject);

		Disposable sub1 = subscribePrint(publishSubject, "First");
		Disposable sub2 = subscribePrint(publishSubject, "Second");

		Disposable sub3 = null;
		try {
			Thread.sleep(300L);

			publishSubject.onNext(555L);
			sub3 = subscribePrint(publishSubject, "Third");
			Thread.sleep(500L);
		} catch (InterruptedException e) {
		}

		sub1.dispose();
		sub2.dispose();
		sub3.dispose();

		try {
			Thread.sleep(500L);
		} catch (InterruptedException e) {
		}

		Disposable sub4 = subscribePrint(publishSubject, "Fourth");

		try {
			Thread.sleep(500L);
		} catch (InterruptedException e) {
		}

		sub4.dispose();

		System.out.println("-----------------------------");

	}

	public static void main(String[] args) {
		new SubjectsDemonstration().run();
	}
}
