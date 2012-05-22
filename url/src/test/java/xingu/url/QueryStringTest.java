package xingu.url;

import static org.junit.Assert.*;

import xingu.url.impl.QueryStringImpl;

import org.junit.Before;
import org.junit.Test;

public class QueryStringTest
{
    private QueryString query;
    
    @Before
    public void createQueryString()
    {
        query = new QueryStringImpl("fruit=apple&color=red");
    }
    
    @Test
    public void testEquals()
    {
        QueryString anotherQuery = new QueryStringImpl("color=red&fruit=apple");
        assertEquals(query, anotherQuery);
    }
    
    @Test
    public void testValue()
    {
        String value = query.get("fruit");
        assertEquals("apple", value);
        
        value = query.get("color");
        assertEquals("red", value);
    }
    
    @Test
    public void testSize()
    {
        int size = query.size();
        assertEquals(2, size);
    }
    
    @Test
    public void testIsQueryString()
    {
        assertTrue(QueryStringImpl.isQueryString("color=blue"));
        assertTrue(QueryStringImpl.isQueryString("color=blue&fruit=watermellon"));
        assertTrue(QueryStringImpl.isQueryString("color=blue fruit=watermellon"));
        assertFalse(QueryStringImpl.isQueryString("color blue&fruit=watermellon"));
        assertFalse(QueryStringImpl.isQueryString("color blue fruit watermellon"));
    }
}
