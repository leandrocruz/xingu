package xingu.http.client;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CookieFilterTest
{
	@Test
	public void testSecureFilter()
	{
		Cookies cookies = CookieUtils.toCookies("id=1; Secure=true");
		assertEquals(1, cookies.size());
	
		Cookies filtered = cookies.filter(new CookieFilter());
		assertEquals(1, filtered.size());
		
		filtered = cookies.filter(new CookieFilter().secure(true));
		assertEquals(1, filtered.size());

		filtered = cookies.filter(new CookieFilter().secure(false));
		assertEquals(0, filtered.size());
	}
	
	@Test
	public void testPathFilter()
	{
		Cookies cookies = CookieUtils.toCookies("id=1; path=/x/y");
		assertEquals(1, cookies.size());
		assertEquals("/x/y", cookies.byName("id").getPath());
		
		Cookies filtered = cookies.filter(new CookieFilter());
		assertEquals(1, filtered.size());

		filtered = cookies.filter(new CookieFilter().path("/x/y"));
		assertEquals(1, filtered.size());

		filtered = cookies.filter(new CookieFilter().path("/x/y/z"));
		assertEquals(1, filtered.size());
		
		filtered = cookies.filter(new CookieFilter().path("/x"));
		assertEquals(0, filtered.size());

		filtered = cookies.filter(new CookieFilter().path("/y"));
		assertEquals(0, filtered.size());
	}

	@Test
	public void testDomainFilter()
	{
		Cookies cookies = CookieUtils.toCookies("id=1; Domain=sub.abc.com");
		assertEquals(1, cookies.size());
		assertEquals("sub.abc.com", cookies.byName("id").getDomain());
		
		Cookies filtered = cookies.filter(new CookieFilter());
		assertEquals(1, filtered.size());
		assertEquals("sub.abc.com", filtered.byName("id").getDomain());

		filtered = cookies.filter(new CookieFilter().domain("sub.abc.com"));
		assertEquals(1, filtered.size());
		assertEquals("sub.abc.com", filtered.byName("id").getDomain());

		filtered = cookies.filter(new CookieFilter().domain("under.sub.abc.com"));
		assertEquals(1, filtered.size());
		assertEquals("sub.abc.com", filtered.byName("id").getDomain());

		filtered = cookies.filter(new CookieFilter().domain("xyz.com"));
		assertEquals(0, filtered.size());

		filtered = cookies.filter(new CookieFilter().domain("abc.com"));
		assertEquals(0, filtered.size());
	}
	
	@Test
	public void testDomainAndPathFilter()
	{
		Cookies cookies = CookieUtils.toCookies("id=1; Domain=sub.abc.com; Path=/x");
		assertEquals(1, cookies.size());
		assertEquals("sub.abc.com", cookies.byName("id").getDomain());
		assertEquals("/x", cookies.byName("id").getPath());
		
		Cookies filtered = cookies.filter(new CookieFilter());
		assertEquals(1, filtered.size());

		filtered = cookies.filter(new CookieFilter().domain("sub.abc.com").path("/x"));
		assertEquals(1, filtered.size());

		filtered = cookies.filter(new CookieFilter().domain("x.sub.abc.com").path("/x"));
		assertEquals(1, filtered.size());
	}
}