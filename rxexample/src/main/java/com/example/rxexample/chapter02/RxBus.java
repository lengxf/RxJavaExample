package com.example.rxexample.chapter02;

import io.reactivex.Flowable;
import io.reactivex.processors.BehaviorProcessor;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;


public class RxBus {

    private static final String TAG = RxBus.class.getSimpleName();

    private FlowableProcessor<Object> rxBus;
    private FlowableProcessor<Object> rxStickBus;

    private RxBus() {
        rxBus = PublishProcessor.create().toSerialized();
        rxStickBus = BehaviorProcessor.create().toSerialized();
    }

    public static RxBus getInstance() {
        return SingletonHolder.instance;
    }

    public void postEvent(Object event) {
        if (this.hasSubscribers()) rxBus.onNext(event);
    }

    public void postStickEvent(Object event) {
        rxStickBus.onNext(event);
    }

    public <T> Flowable<T> toFlowable(Class<T> type) {
        return rxBus.hide().ofType(type).onBackpressureBuffer();
    }

    public <T> Flowable<T> toStickFlowable(Class<T> type) {
        return rxStickBus.hide().ofType(type).onBackpressureBuffer();
    }

    private boolean hasSubscribers() {
        return rxBus.hasSubscribers();
    }

    @Deprecated
    public boolean hasStickSubscribers() {
        return rxStickBus.hasSubscribers();
    }

    private static class SingletonHolder {

        private static RxBus instance = new RxBus();
    }
}