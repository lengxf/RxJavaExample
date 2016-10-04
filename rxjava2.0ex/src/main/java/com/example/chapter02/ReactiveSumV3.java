package com.example.chapter02;


import com.example.common.Program;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

import static com.example.common.Helpers.subscribePrint;

/**
 * Another version of the 'Reactive Sum', this time it is implemented, using {@link BehaviorSubject}s.
 * 
 * @author meddle
 */
public class ReactiveSumV3 implements Program {
	
	public static class ReactiveSum {
		private BehaviorSubject<Double> a = BehaviorSubject.createDefault(0.0);
		private BehaviorSubject<Double> b = BehaviorSubject.createDefault(0.0);
		private BehaviorSubject<Double> c = BehaviorSubject.createDefault(0.0);

		public ReactiveSum() {
			Observable.combineLatest(a, b, (x, y) -> x + y).subscribe(c);
		}

		public double getA() {
			return a.getValue();
		}

		public void setA(double a) {
			this.a.onNext(a);
		}

		public Observable<Double> obsA() {
			return a.hide();
		}

		public double getB() {
			return b.getValue();
		}

		public void setB(double b) {
			this.b.onNext(b);
		}

		public Observable<Double> obsB() {
			return b.hide();
		}

		public double getC() {
			return c.getValue();
		}

		public Observable<Double> obsC() {
			return c.hide();
		}

	}

	@Override
	public String name() {
		return "Reactive Sum, version 3 (with Subjects)";
	}

	@Override
	public int chapter() {
		return 3;
	}

	@Override
	public void run() {
		ReactiveSum sum = new ReactiveSum();

		subscribePrint(sum.obsC(), "Sum");
		sum.setA(5);
		sum.setB(4);
	}
	
	public static void main(String[] args) {
		new ReactiveSumV3().run();
	}

}
