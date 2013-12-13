package xingu.http.client.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jboss.netty.handler.codec.http.Cookie;

import xingu.http.client.Cookies;

public class CookiesImpl
	implements Cookies
{
	private Set<Cookie> set;
	
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
}
