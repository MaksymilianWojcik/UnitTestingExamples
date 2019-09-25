package com.example.unittestingexamples.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Random;

public class DateUtilTest {

    public static final String today = "09-2019";

    @Test
    void testGetCurrentTimestamp_returnedTimestamp() {
        Assertions.assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                Assertions.assertEquals(today, DateUtil.getCurrentTimeStamp());
                System.out.println("Timestamp generated correctly");
            }
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
    void getMonthFromNumber_returnSuccess(int monthNumber, TestInfo testInfo, TestReporter testReporter) {
        Assertions.assertEquals(DateUtil.months[monthNumber], DateUtil.getMonthFromNumber(DateUtil.monthNumbers[monthNumber]));
        System.out.println(DateUtil.monthNumbers[monthNumber] + " : " + DateUtil.months[monthNumber]);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11})
    void getMonthFromNumber_returnError(int monthNumber, TestInfo testInfo, TestReporter testReporter) {
        int randomInt = new Random().nextInt(90) + 13;
        Assertions.assertEquals(DateUtil.getMonthFromNumber(String.valueOf(monthNumber * randomInt)), DateUtil.GET_MONTH_ERROR);
        System.out.println(randomInt + " : " + DateUtil.GET_MONTH_ERROR);
    }

}
