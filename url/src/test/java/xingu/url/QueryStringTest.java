package xingu.url;

import static org.junit.Assert.*;

import java.util.List;

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
    
    @Test
    public void testDecoding()
    {
    	
       /*  ISO-8859-1
    	*   				  document.title  = "Cinemas, Séries & Livros"
    	*  encodeURIComponent(document.title) = "Cinemas%2C%20S%C3%A9ries%20%26%20Livros"
    	*   		encodeURI(document.title) = "Cinemas,%20S%C3%A9ries%20&%20Livros"
    	*              escape(document.title) = "Cinemas%2C%20S%E9ries%20%26%20Livros"
    	*/
    	
    	QueryString encoded = new QueryStringImpl("a=%D0%9F%D1%80%D0%B0%D0%B2%D0%B4%D0%B0.%D0%A0%D1%83%3A%20%D0%90%D0%BD%D0%B0%D0%BB%D0%B8%D1%82%D0%B8%D0%BA%D0%B0%20%D0%B8%20%D0%BD%D0%BE%D0%B2%D0%BE%D1%81%D1%82%D0%B8");
    	System.out.println(encoded.getDecoded("a", "UTF-8"));
    }
    
    @Test
    public void testRepetition()
    	throws Exception
    {
    	QueryStringImpl qs = new QueryStringImpl("fruit=apple&fruit=orange&color=red");
    	String apple = qs.get("fruit");
    	assertEquals("apple", apple);
    	
    	List<String> all = qs.getAll("fruit");
    	assertEquals(2, all.size());
    	assertEquals("apple", all.get(0));
    	assertEquals("orange", all.get(1));
    
    	assertEquals("red", qs.get("color"));
    }
}