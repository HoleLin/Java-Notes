package com.holelin.sundry.test;

import cn.hutool.core.lang.hash.Hash32;

import java.time.LocalDate;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @Description:
 * @Author: HoleLin
 * @CreateDate: 2022/3/14 1:46 PM
 * @UpdateUser: HoleLin
 * @UpdateDate: 2022/3/14 1:46 PM
 * @UpdateRemark: 修改内容
 * @Version: 1.0
 */
public class LambdaTest {
    public static void main(String[] args) {
        Stream.iterate(LocalDate.now(), it -> it.plusDays(1))
                .limit(5)
                .forEach(System.out::println);
        Stream.generate(Math::random)
                .limit(10)
                .forEach(System.out::println);
        IntStream.of(1, 2, 3, 4).boxed().collect(Collectors.toList()).forEach(System.out::println);
        System.out.println(IntStream.rangeClosed(1, 10).reduce(Integer::sum).orElse(0));
        System.out.println(IntStream.rangeClosed(1, 10).reduce(0, (x, y) -> x + 2 * y));
        IntStream.rangeClosed(1, 10)
                .peek(n -> System.out.printf("original:%d%n", n))
                .map(n -> n * 2)
                .peek(n -> System.out.printf("doubled: %d%n", n))
                .filter(n -> n % 3 == 0)
                .peek(n -> System.out.printf("filtered: %d%n", n))
                .sum();
        IntStream.range(100, 200)
                .map(LambdaTest::multByTwo)
                .filter(LambdaTest::divByThree)
                .findFirst();

        Collector.of(TreeSet::new,
                SortedSet<String>::add,
                (left, right) -> {
                    left.addAll(right);
                    return left;
                },
                Collections::unmodifiableNavigableSet);
        Function<Integer, Integer> add = x -> x + 2;
        Function<Integer, Integer> mult = x -> x * 3;
        final Function<Integer, Integer> multadd = add.compose(mult);
        final Function<Integer, Integer> addmult = add.andThen(mult);
        System.out.println("multadd(1): " + multadd.apply(1));
        System.out.println("addmult(1): " + addmult.apply(1));
        System.out.println(Runtime.getRuntime().availableProcessors());

    }

    public static int multByTwo(int n) {
        System.out.printf("Inside multByTwo with arg %d%n", n);
        return n * 2;
    }

    public static boolean divByThree(int n) {
        System.out.printf("Inside divByThree with arg %d%n", n);
        return n % 3 == 0;
    }
}
