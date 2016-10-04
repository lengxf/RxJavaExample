package com.example.chapter02;


import com.example.common.Program;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.example.common.Helpers.subscribePrint;

/**
 * Show case of the Observable.create method.
 * Contains a simple implementation of the Observable.from(Iterable) method, using Observable.create.
 *
 * @author meddle
 */
public class ObservableCreateExample implements Program {

    public static <T> Observable<T> fromIterable(final Iterable<T> iterable) {
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                try {
                    Iterator<T> iterator = iterable.iterator();

                    while (iterator.hasNext()) {
                        emitter.onNext(iterator.next());
                    }

                    if (!emitter.isDisposed()) {
                        emitter.onComplete();
                    }
                } catch (Exception e) {
                    if (!emitter.isDisposed()) {
                        emitter.onError(e);
                    }
                }

            }
        });

    }

    public static void main(String[] args) {
        new ObservableCreateExample().run();
    }

    @Override
    public String name() {
        return "Demonstration of the Observable.create method";
    }

    @Override
    public int chapter() {
        return 3;
    }

    @Override
    public void run() {
        subscribePrint(fromIterable(Arrays.asList(1, 3, 5)), "List1");
        subscribePrint(fromIterable(Arrays.asList(2, 4, 6)), "List2");

        try {
            Path path = Paths.get("src", "main", "resources", "lorem_big.txt");
            List<String> data = Files.readAllLines(path);

            Observable<String> observable = fromIterable(data).subscribeOn(
                    Schedulers.computation());

            Disposable disposable = subscribePrint(observable, "File");
            System.out.println("Before unsubscribe!");
            System.out.println("-------------------");

            disposable.dispose();

            System.out.println("-------------------");
            System.out.println("After unsubscribe!");

            Thread.sleep(100L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
