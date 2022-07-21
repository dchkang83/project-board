package com.main.gundam.sample;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class SampleTests {
    @BeforeAll
    public static void beforeAll() {
        System.out.println("########## beforeAll");
    }

    @BeforeEach
    public void beforeEach() {
        System.out.println("##### beforeEach");

    }

    @AfterEach
    public void afterEach() {
        System.out.println("##### afterEach");
    }

    @AfterAll
    public static void afterAll() {
        System.out.println("########## afterAll");
    }

    @Test
    @DisplayName("기본적인 테스트 테스트")
    public void test0001() {
        System.out.println("test0001");

        final int[] numbers = {-3, -5, 1, 7, 4, -2};
        final int[] expected = {-5, -3, -2, 1, 4, 7};

        Arrays.sort(numbers);
        assertArrayEquals(expected, numbers);
    }

    @ParameterizedTest
    @DisplayName("String parameters")
    @ValueSource(strings = {
        "a", "b", "d", "e", "f"
    })
    public void argsStringsTest(final String s) {
        System.out.println(s);
    }

    // @ParameterizedTest
    // @DisplayName("Ints parameters")
    // @ValueSource(ints = {
    //     1, 2, 3, 4, 5
    // })
    // public void argsInts1Test(final Integer i) {

    //     Assertions.assertEquals(3, i);
    // }

    @ParameterizedTest
    @DisplayName("Ints parameters")
    @ValueSource(ints = {
        1, 2, 3, 4, 5
    })
    public void argsInts2Test(final Integer i) {

        Assertions.assertTrue(convert(i));
    }

    private boolean convert(Integer i) {
        return i < 6;
    }
}
