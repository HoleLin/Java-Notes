package cn.holelin.rxjava.obserables;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.core.SingleOnSubscribe;
import org.junit.jupiter.api.Test;

public class SingleTest {

    @Test
    void test_single() {
        Single.create((SingleOnSubscribe<String>) emitter -> emitter.onSuccess("test"))
                .subscribe(
                        System.out::println,
                        throwable -> throwable.printStackTrace()
                );
    }
}
