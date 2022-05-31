package cn.holelin.rxjava.obserables;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.junit.jupiter.api.Test;

public class ObservableTest {


    @Test
    void test_subscribeOn() {
        Observable.create(emitter -> {
                    emitter.onNext("hello");
                    emitter.onNext("world");
                }).subscribeOn(Schedulers.newThread())
                .subscribe(System.out::println);
    }

    @Test
    void test_observerOn() {
        Observable.create(emitter -> {
                    emitter.onNext("hello");
                    emitter.onNext("world");
                }).observeOn(Schedulers.io())
                .subscribe(System.out::println);
    }

    @Test
    void test_lifecycle() {
        Observable.just("Hello")
                .doOnNext(s -> System.out.println("doOnNext: " + s))
                .doAfterNext(s -> System.out.println("doAfterNext: " + s))
                .doOnComplete(() -> System.out.println("doOnComplete"))
                .doOnSubscribe(disposable -> System.out.println("doOnSubscribe"))
                .doAfterTerminate(() -> System.out.println("doAfterTerminate"))
                .doFinally(() -> System.out.println("doFinally"))
                .doOnEach(stringNotification -> System.out.println("doOnEach: " + (stringNotification.isOnNext() ? "onNext" : stringNotification.isOnComplete() ? "onComplete" : "onError")))
                .doOnLifecycle(
                        disposable -> System.out.println("doOnLifecycle: " + disposable.isDisposed()),
                        () -> System.out.println("doOnLifecycle run:"))
                .subscribe(s -> System.out.println("收到的消息:" + s));
    }



}
