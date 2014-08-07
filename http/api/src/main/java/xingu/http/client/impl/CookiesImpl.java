package xingu.http.client.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.DefaultCookie;

import xingu.http.client.Cookies;

public class CookiesImpl
	implements Cookies
{
	private Set<Cookie> set;
	
	public CookiesImpl(Cookie... cookies)
	{
		set = new HashSet<Cookie>();
		for(Cookie c : cookies)
		{
			set.add(c);
		}
	}
	
	public CookiesImpl(Set<Cookie> cookies)
	{
		this.set = cookies;
	}

	@Override
	public Cookie byName(String name)
	{
		for(Cookie c : set)
		{
			if(c.getName().equals(name))
			{
				return c;
			}
		}
		return null;
	}
	
	@Override
	public Cookie startingWith(String name)
	{
		for(Cookie c : set)
		{
			if(c.getName().startsWith(name))
			{
				return c;
			}
		}
		return null;
	}

	@Override
	public Set<Cookie> set()
	{
		return set;
	}

	@Override
	public List<String> names()
	{
		List<String> result = new ArrayList<String>();
		for(Cookie c : set)
		{
			result.add(c.getName());
		}
		return result;
	}

	@Override
	public Cookie replace(String name, String content)
	{
		DefaultCookie cookie = new DefaultCookie(name, content);
		replace(name, cookie);
		
		return cookie;
	}

	@Override
	public void replace(String name, Cookie replacement)
	{
		Cookie toRemove = null;
		for(Cookie c : set)
		{
			if(c.getName().startsWith(name))
			{
				toRemove = c;
				break;
			}
		}
		if(toRemove != null)
		{
			set.remove(toRemove);
			set.add(replacement);
		}
	}

	@Override
	public int size()
	{
		return set.size();
	}

	@Override
	public void add(Cookie cookie)
	{
		set.add(cookie);
	}
}