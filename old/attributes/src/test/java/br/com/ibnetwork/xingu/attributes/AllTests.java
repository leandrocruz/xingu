package br.com.ibnetwork.xingu.attributes;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses(value = {
        AttributeTypeManagerTest.class,
        AttributeListManagerXMLTest.class,
        AttributeListManagerDBTest.class,
        AttributeListXMLTest.class,
        AttributeListDBTest.class
})

public class AllTests 
{}
