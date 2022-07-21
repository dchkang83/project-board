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

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SampleTests {
    @BeforeAll
    public static void beforeAll() {
        log.info("########## beforeAll");
    }

    @BeforeEach
    public void beforeEach() {
        log.info("##### beforeEach");

    }

    @AfterEach
    public void afterEach() {
        log.info("##### afterEach");
    }

    @AfterAll
    public static void afterAll() {
        log.info("########## afterAll");
    }

    @Test
    @DisplayName("기본적인 테스트 테스트")
    public void test0001() {
        log.info("test0001");

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
        log.info(s);
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
