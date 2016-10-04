package com.example.common;

import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;


/**
 * Contains a set of helper methods, used in the examples.
 *
 * @author meddle
 */
public final class Helpers {

    public static <T> Disposable subscribePrint(Observable<T> observable,
                                                String name) {
        return observable.subscribe(
                (v) -> System.out.println(Thread.currentThread().getName()
                        + "|" + name + " : " + v),
                (e) -> {
                    System.err.println("Error from " + name + ":");
                    System.err.println(e);
                    System.err.println(
                            e.getStackTrace()
                    );
                },
                () -> System.out.println(name + " ended!"));
    }

    /**
     * Subscribes to an observable, printing all its emissions.
     * Blocks until the observable calls onCompleted or onError.
     */
    public static <T> void blockingSubscribePrint(Observable<T> observable, String name) {
        CountDownLatch latch = new CountDownLatch(1);
        subscribePrint(observable.doAfterTerminate(() -> latch.countDown()), name);
        try {
            latch.await();
        } catch (InterruptedException e) {
        }
    }

    public static <T> Consumer<Notification<? super T>> debug(String description) {
        return debug(description, "");
    }

    public static <T> Consumer<Notification<? super T>> debug(String description, String offset) {
        AtomicReference<String> nextOffset = new AtomicReference<String>(">");

        return (Notification<? super T> notification) -> {
            if (notification.isOnNext()){
                System.out.println(
                        Thread.currentThread().getName() +
                                "|" + description + ": " + offset +
                                nextOffset.get() +
                                notification.getValue()
                );
            }
            if (notification.isOnComplete()){
                System.out.println(Thread.currentThread().getName() +
                        "|" + description + ": " + offset +
                        nextOffset.get() + "|"
                );
            }
            if (notification.isOnError()){
                System.err.println(Thread.currentThread().getName() +
                        "|" + description + ": " + offset +
                        nextOffset.get() + " X " + notification.getError());
            }

            String d=nextOffset.get();
            nextOffset.compareAndSet(d,"-"+d);
        };
    }

}