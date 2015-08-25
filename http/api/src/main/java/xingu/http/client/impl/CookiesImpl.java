package xingu.http.client.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.DefaultCookie;

import xingu.http.client.Cookies;

public class CookiesImpl
	implements Cookies
{
	private Set<Cookie> buffer;
	
	public CookiesImpl()
	{
		buffer = new HashSet<Cookie>();
	}

	public CookiesImpl(Cookie... cookies)
	{
		buffer = new HashSet<Cookie>();
		for(Cookie c : cookies)
		{
			buffer.add(c);
		}
	}
	
	public CookiesImpl(Set<Cookie> cookies)
	{
		this.buffer = cookies;
	}

	@Override
	public Cookie byName(String name)
	{
		for(Cookie c : buffer)
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
		for(Cookie c : buffer)
		{
			if(c.getName().startsWith(name))
			{
				return c;
			}
		}
		return null;
	}

	@Override
	public Set<Cookie> getBuffer()
	{
		return buffer;
	}

	public void setBuffer(Set<Cookie> data)
	{
		this.buffer = data;
	}

	@Override
	public List<String> names()
	{
		List<String> result = new ArrayList<String>();
		for(Cookie c : buffer)
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
		for(Cookie c : buffer)
		{
			if(c.getName().startsWith(name))
			{
				toRemove = c;
				break;
			}
		}
		if(toRemove != null)
		{
			buffer.remove(toRemove);
			buffer.add(replacement);
		}
	}

	@Override
	public int size()
	{
		return buffer.size();
	}

	@Override
	public void add(Cookie cookie)
	{
		buffer.add(cookie);
	}

	@Override
	public String toString()
	{
		return names().toString();
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof Cookies))
		{
			return false;
		}
		
		if(obj == this)
		{
			return true;
		}
		
		Cookies other = Cookies.class.cast(obj);
		Set<Cookie> cookies = other.getBuffer();
		if(buffer.size() != cookies.size())
		{
			return false;
		}

		for(Cookie cookie : buffer)
		{
			String name = cookie.getName();
			Cookie otherCookie = other.byName(name);
			if(!Objects.equals(cookie, otherCookie))
				return false;
		}

		return true;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(buffer);
	}

}