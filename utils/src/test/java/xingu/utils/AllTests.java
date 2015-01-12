package xingu.utils;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import xingu.utils.sorting.SortUtilsTest;

@RunWith(Suite.class)
@SuiteClasses(value = {
        ArrayUtilsTest.class, 
        SortUtilsTest.class,
        FSUtilsTest.class,
        FieldUtilsTest.class
})

public class AllTests
{}
