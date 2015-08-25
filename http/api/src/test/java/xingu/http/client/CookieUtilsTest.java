package xingu.http.client;

import static org.junit.Assert.*;

import org.jboss.netty.handler.codec.http.DefaultCookie;
import org.junit.Test;

import xingu.http.client.impl.CookiesImpl;

public class CookieUtilsTest
{
	@Test
	public void testParse()
		throws Exception
	{
		Cookies cookies = CookieUtils.toCookies("my_cookie=1; my_other_cookie=2");
		assertEquals(2, cookies.size());
		
		assertEquals("1", cookies.byName("my_cookie").getValue());
		assertEquals("2", cookies.byName("my_other_cookie").getValue());
	}

	@Test
	public void testMergeEmpty()
		throws Exception
	{
		Cookies c1 = new CookiesImpl();
		Cookies c2 = new CookiesImpl();
		
		Cookies merged = CookieUtils.mergeCookies(c1, c2);
		assertEquals(0, merged.size());
	}

	@Test
	public void testMergeAppendOnEmpty()
		throws Exception
	{
		Cookies c1 = new CookiesImpl();
		Cookies c2 = new CookiesImpl(new DefaultCookie("my_cookie", "b"));
		
		Cookies merged = CookieUtils.mergeCookies(c1, c2);
		assertEquals(0, c1.size());
		assertEquals(1, c2.size());
		assertEquals(1, merged.size());
		
		assertEquals("b", c2.byName("my_cookie").getValue());
		assertEquals("b", merged.byName("my_cookie").getValue());
	}

	@Test
	public void testMergeReplace()
		throws Exception
	{
		Cookies c1 = new CookiesImpl(new DefaultCookie("my_cookie", "a"));
		Cookies c2 = new CookiesImpl(new DefaultCookie("my_cookie", "b"));
		
		Cookies merged = CookieUtils.mergeCookies(c1, c2);
		assertEquals(1, c1.size());
		assertEquals(1, c2.size());
		assertEquals(1, merged.size());
	
		assertEquals("a", c1.byName("my_cookie").getValue());
		assertEquals("b", c2.byName("my_cookie").getValue());
		assertEquals("b", merged.byName("my_cookie").getValue());
	}

	@Test
	public void testMergeReplace2()
		throws Exception
	{
		Cookies c1 = new CookiesImpl(new DefaultCookie("my_cookie", "a"));
		Cookies c2 = new CookiesImpl(new DefaultCookie("my_cookie", "b"), new DefaultCookie("my_other_cookie", "c"));
		
		Cookies merged = CookieUtils.mergeCookies(c1, c2);
		assertEquals(1, c1.size());
		assertEquals(2, c2.size());
		assertEquals(2, merged.size());
	
		assertEquals("a", c1.byName("my_cookie").getValue());
		assertEquals("b", c2.byName("my_cookie").getValue());
		assertEquals("c", c2.byName("my_other_cookie").getValue());
		
		assertEquals("b", merged.byName("my_cookie").getValue());
		assertEquals("c", merged.byName("my_other_cookie").getValue());
	}

	@Test
	public void testMergeReplace3()
		throws Exception
	{
		Cookies c1 = new CookiesImpl(new DefaultCookie("my_cookie", "a"));
		Cookies c2 = new CookiesImpl(new DefaultCookie("my_other_cookie", "c"));
		
		Cookies merged = CookieUtils.mergeCookies(c1, c2);
		assertEquals(1, c1.size());
		assertEquals(1, c2.size());
		assertEquals(2, merged.size());
	
		assertEquals("a", c1.byName("my_cookie").getValue());
		assertEquals("c", c2.byName("my_other_cookie").getValue());
		
		assertEquals("a", merged.byName("my_cookie").getValue());
		assertEquals("c", merged.byName("my_other_cookie").getValue());
	}
}