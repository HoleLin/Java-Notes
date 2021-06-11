package com.holelin.sundry.test.toolset;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class GuavaTest {
    @Test
    public void testJoiner() {
        String[] testStrings = new String[]{"Java", "Android", "C++", "C", "C#"};
        String expectResult = "Java; Android; C++; C; C#";
        String result = Joiner.on("; ").join(testStrings);
        assertThat(expectResult, equalTo(result));
    }

    @Test
    public void testJoinerAppendTo() {
        String[] testStrings = new String[]{"Java", "Android", "C++", "C", "C#"};
        String expectResult = "Guava; Java; Android; C++; C; C#";
        StringBuilder sb = new StringBuilder().append("Guava; ");
        StringBuilder stringBuilder = Joiner.on("; ").appendTo(sb, testStrings);
        assertThat(expectResult, equalTo(stringBuilder.toString()));
    }

    @Test
    public void testJoinerSkipNull() {
        String[] testStrings = new String[]{"Java", null, "C++", "C", "C#"};
        String expectResult = "Java; C++; C; C#";
        String result = Joiner.on("; ").skipNulls().join(testStrings);
        assertThat(expectResult, equalTo(result));
    }

    @Test
    public void testJoinerUseForNull() {
        String[] testStrings = new String[]{"Java", null, "C++", "C", "C#"};
        String expectResult = "Java; Default; C++; C; C#";
        String result = Joiner.on("; ").useForNull("Default").join(testStrings);
        assertThat(expectResult, equalTo(result));
    }

    @Test
    public void testJoinerMap() {
        Map<String, Object> map = new HashMap<>();
        for (int i = 1; i <= 3; i++) {
            map.put(String.valueOf(i), i);
        }
        String expectResult = "1:1; 2:2; 3:3";
        String result = Joiner.on("; ").withKeyValueSeparator(":").join(map);
        assertThat(expectResult, equalTo(result));
    }

    @Test
    public void testSplitter() {
        String test = "a1;b2;c3";
        List<String> strings = Splitter.on(";").splitToList(test);
        List<String> expectResult = new ArrayList<>();
        expectResult.add("a1");
        expectResult.add("b2");
        expectResult.add("c3");
        assertThat(expectResult,equalTo(strings));
    }

    @Test
    public void testSplitterFixedLength() {
        String test = "aabbccd";
        List<String> strings = Splitter.fixedLength(2).splitToList(test);
        List<String> expectResult = new ArrayList<>();
        expectResult.add("aa");
        expectResult.add("bb");
        expectResult.add("cc");
        expectResult.add("d");
        assertThat(expectResult, equalTo(strings));
    }
    @Test
    public void testOrdering() {

        Set<String> wordsWithPrimeLength = ImmutableSet.of("one", "two", "three", "six", "seven", "eight");
        Set<String> primes = ImmutableSet.of("two", "three", "five", "seven");
        Sets.SetView<String> intersection = Sets.intersection(primes,wordsWithPrimeLength);
        HashSet<String> strings = intersection.copyInto(Sets.newHashSet());
        ImmutableSet<String> strings2 = intersection.immutableCopy();
        strings.add("111");
        System.out.println(strings);
        System.out.println(strings2);
    }
}
