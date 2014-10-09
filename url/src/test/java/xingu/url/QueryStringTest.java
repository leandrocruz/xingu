package xingu.url;

import static org.junit.Assert.*;

import java.net.URLEncoder;
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
    	throws Exception
    {
    	
       /*  ISO-8859-1
    	*   				  document.title  = "Cinemas, Séries & Livros"
    	*  encodeURIComponent(document.title) = "Cinemas%2C%20S%C3%A9ries%20%26%20Livros"
    	*   		encodeURI(document.title) = "Cinemas,%20S%C3%A9ries%20&%20Livros"
    	*              escape(document.title) = "Cinemas%2C%20S%E9ries%20%26%20Livros"
    	*/
    	
    	String      charset = "UTF-8";
		String      input   = "Cinemas, Séries & Livros";
		String      value   = new String(input.getBytes(charset));
    	String      encoded = URLEncoder.encode(value, charset);
    	QueryString qs      = new QueryStringImpl("a="+encoded);
    	assertEquals(input, qs.getDecoded("a", charset));
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