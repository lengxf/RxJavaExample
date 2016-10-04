https://github.com/ReactiveX/RxJava/pull/3348

Here are some ideas on API design and comparisons between the types:

- Push
```
Observable.create(s -> {
   s.onNext(t)
   ...
   s.onNext(t)
})
```
would be equivalent to this if we chose to have these APIs:

```
Flowable.createPush(s -> {
   s.onNext(t)
   ...
   s.onNext(t)
}, BackpressureStrategy.BUFFER)
```

- Pull-Push/Async Pull

Observable does not support pull.

With Flowable there is synchronous variety:
```
Flowable.createSync(... SyncOnSubscribe ... )
// or
Flowable.from(Iterable)
// or
Flowable.just(T...)
// or
Flowable.range(0, 10000000)
```
and an asynchronous variety:
```
Flowable.createAsync(... AsyncOnSubscribe ... )
```
- Conversion

From Flowable to Observable, it is easy since it asks for no request(n) flow control:

Flowable f = ...
Observable<T> o = f.toObservable(); // this will call request(Long.MAX_VALUE) up when subscribed to
From Observable to Flowable, it must provide a backpressure strategy:

Observable o = ...
Flowable<T> f = o.toFlowable(BackpressureStrategy.*)
// for example
Flowable<T> f = o.toFlowable(BackpressureStrategy.BUFFER)
// or 
Flowable<T> f = o.toFlowable(BackpressureStrategy.DROP)
// or
Flowable<T> f = o.toFlowable(BackpressureStrategy.FAIL)
// or 
Flowable<T> f = o.toFlowable(BackpressureStrategy.create(...)) // l



Here is an attempt at separating out behavior between Observable and Flowable:

Observable : async push without backpressure
Flowable : async pull, and push/pull with backpressure via batch requesting

Below I separate out the operators (such as temporal flow control) and creators/generators as applicable to each type.

I'd appreciate feedback on this direction.

Observable

This is for creating a true "push" Observable without backpressure

Observable.create(Observer o) :: Observable
The subscribe behavior should have take an Observer without backpressure

subscribe(Observer<? super T>)
A key question is whether Observable should implement the Publisher interface for seemless interop between Flowable and Observable. To be true to the types, Observable should not implement Publisher and we would need to call obs.toFlowable(strategy) when interacting with Flowables subscribe(Subscriber<? super T>) // if we implement this, then we need a default OnBackpressureStrategy that would throw MissingBackpressureException

This would exist to convert to a Flowable

toFlowable(OnBackpressureStrategy strategy) :: Flowable



**benjchristensen commented on 29 Aug 2015:**

It was mentioned to me that it may help to restate what I'm seeking for here as it could be interpreted as a theoretical and polarized argument. Good point, so I'll try. It is somewhat long.

I am open to having 2 types in RxJava to represent bounded/unbounded, hot/cold, Observable/Flowable.

However, I do want solid reasons for the decisions, as we spent over a year debating many proposals in the 0.x phase before landing on an Observable with reactive-pull backpressure and choosing to adopt it as the single type. The community, and @headinthebox, were involved in that design and decision.

I and others then spent many months of debate formalizing the pattern in Reactive Streams, demonstrating across many companies and projects that a async stream type with reactive-pull backpressure is valid and wanted. Those types are now proposed for inclusion in JDK 9 (j.u.c.Flow), demonstrating fairly broad agreement.

In the time since while using the reactive-pull Observable type, I have not found a streaming use case it does not work well for, including "hot" streams. In fact, I find the backpressure signal to be great on "hot" streams, as it forces me to consider how to behave when production is faster than slower, and I have the tools to apply a strategy.

That said, I also recognize that in many environments, an Observable with unbounded buffers works just fine when only working with small finite streams of data, particularly request/response environments (which can use the Single) type.

I prefer not making users have to choose between two virtually identical types (Observable and Flowable) unless there are important functional, usability or performance benefits to doing so.

If we do have two types, here is how I would envision them co-existing:

Observable (no backpressure, unbounded operators)

Creation is as expected, emit however one wishes:

create(s -> {
  onNext(t)
})
Cold generation though should be discouraged, and should return a Flowable. For example:

Observable.from(iterable) -> Flowable
That case should just return a Flowable as that is what it is. Or we just don't have from or just overloads, which would make more sense, since Observable is intended to be "hot", otherwise the type doesn't help communicate the distinction.

The merge/flatMap and observeOn cases would be unbounded with their associated dangers.

I suggest that zip(Observable, Observable) not exist, but allow observable.zipWith(Iterable) or Observable.zipWith(Flowable)

An Observable could become a Flowable with a backpressure strategy:

observable.toFlowable(Strategy.DROP).
Flowable (backpressure, bounded operators)

The Flowable type could be used for both "hot" or "cold", but "hot" Flowable instances would always have a strategy for dealing with a backpressure signal: drop, buffer, whatever.

Flowable.createHot(s -> {
   s.onNext(...)
}, Strategy.FAIL)

// cold sync generator
Flowable.createSync(...)

// cold async generator
Flowable.createAsync(...)
API Design

How would these two types be used when producing public APIs in libraries? I think it will be more confusing than today where this is just one type unless it is very clear that Observable represents "hot" data. If we are clear in that distinction then it could work well.

/**
* Observable signals a "hot" stream where you must account for flow control yourself or risk unbounded latency and memory growth.
*/
Observable<T> getStuff();

/**
* Flowable signals a "cold" or "hot" stream that will adapt its flow control, or emit an error if it overwhelms your consumption. 
*/
Flowable<T> getStuff2();
The Question

What functional, performance or usability items warrant making people choose between 2 very similar types, when Flowable can do all of the behavior?

Is the "confusion" of backpressure in v1 Observable just because we did a poor job of API design on Observable.create? Or is it more fundamental?

I believe it is just a usability issue of Observable.create that makes people have to think about backpressure too much. I think that issue should effectively be hidden from almost everyone.

I think it's possible to have a single type that is easy to use for all of these use cases ("hot", "cold", backpressured, not-backpressured), and it's purely an implementation detail of the operators.

I think it would be far more confusing to choose when to expose an Observable vs a Flowable than to just have a single type with proper "creation methods" that enable simply creating the correct "hot" or "cold" behavior.

Then again, perhaps it is worth communicating via a type that something is "hot": #2785

Community ... please provide your insight.