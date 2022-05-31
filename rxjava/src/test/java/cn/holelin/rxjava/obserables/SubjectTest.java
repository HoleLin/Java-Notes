package cn.holelin.rxjava.obserables;

import io.reactivex.rxjava3.schedulers.Schedulers;
import io.reactivex.rxjava3.subjects.AsyncSubject;
import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;
import io.reactivex.rxjava3.subjects.ReplaySubject;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

public class SubjectTest {
    @Test
    void test_publish_subject() {
        PublishSubject<Integer> subject = PublishSubject.create();
        subject.onNext(1);
        subject.subscribe(data -> System.out.println("subscribe 1: " + data));
        subject.onNext(2);
        subject.onNext(3);
        subject.onNext(4);
        subject.subscribe(data -> System.out.println("subscribe 2: " + data));
        subject.onNext(5);
        subject.onNext(6);
    }

    @Test
    void test_replay_subject() {
        ReplaySubject<Integer> s = ReplaySubject.create();
        s.subscribe(v -> System.out.println("Early:" + v));
        s.onNext(0);
        s.onNext(1);
        s.subscribe(v -> System.out.println("Late: " + v));
        s.onNext(2);

        ReplaySubject.createWithTimeAndSize(50, TimeUnit.MILLISECONDS,
                Schedulers.single(), 2);
    }

    @Test
    void test_behavior_subject() {
        BehaviorSubject<Integer> s = BehaviorSubject.create();
        s.onNext(0);
        s.onNext(1);
        s.onNext(2);
        s.subscribe(v -> System.out.println("Late: " + v));
        s.onNext(3);
    }

    @Test
    void test_async_subject(){
        AsyncSubject<Integer> s = AsyncSubject.create();
        s.subscribe(v -> System.out.println(v));
        s.onNext(0);
        s.onNext(1);
        s.onNext(2);
        s.onComplete();
        s.publish().share();
    }

}
