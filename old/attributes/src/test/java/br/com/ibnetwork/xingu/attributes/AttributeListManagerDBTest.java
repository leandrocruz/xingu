package br.com.ibnetwork.xingu.attributes;

import static org.junit.Assert.assertEquals;

public class AttributeListManagerDBTest
    extends AttributeListManagerTest
{
    public AttributeListManagerDBTest()
    {
        componentKey = "db"; 
        assertEquals("db", componentKey);
    }
}
