package xingu.http.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.CookieDecoder;

import xingu.http.client.impl.CookiesImpl;

public class CookieUtils
{
	public static final Cookie[] EMPTY = new Cookie[]{};

	private static CookieDecoder decoder = new CookieDecoder();

	public static Cookies toCookies(String input)
	{
		Set<Cookie>   decoded = decoder.decode(input);
		return new CookiesImpl(decoded);
	}
	
	public static Cookies getCookies(HttpResponse res)
    {
        /*
         * Sometimes, multiple set-cookie headers are sent for the same cookie.
         * We need to parse all 'set-cookie' headers, but store only the last
         */
        Map<String, Cookie> cookieByName = new HashMap<>();

        NameValue[] headers = res.getHeaders();
        for(NameValue header : headers)
        {
            String name = header.getName();
            if("set-cookie".equalsIgnoreCase(name))
            {
                String value = header.getValue();
                
                // Workaround for removing an invalid 
                value = cleanValue(value);
                
                Set<Cookie> cookies = decoder.decode(value);
                for(Cookie cookie : cookies)
                {
                    cookieByName.put(cookie.getName(), cookie);
                }
            }
        }

        Set<Cookie> decoded = new TreeSet<>();
        for(String name : cookieByName.keySet())
        {
            decoded.add(cookieByName.get(name));
        }
        
        return new CookiesImpl(decoded);
    }
    
    private static String cleanValue(String v)
    {
    	String[] values = v.split(";");
    	String   v2     = "";
    	
    	for(String value : values)
    	{
    		int i = value.indexOf(":");
            int j = value.indexOf("=");
            
            if((i != -1 && i < j) ||
                (i != -1 && j == -1))
            {
            	value = value.replaceFirst(": ", "=");
            }
            
            v2 += value + "; ";
    	}
    	
        if(StringUtils.isEmpty(v2))
        {
        	return v;
        }
        
        return v2;
    }
	
//	public static Cookies getCookies(HttpResponse res)
//	{
//
//		/*
//		 * Sometimes, multiple set-cookie headers are sent for the same cookie.
//		 * We need to parse all 'set-cookie' headers, but store only the last
//		 */
//		Map<String, Cookie> cookieByName = new HashMap<>();
//
//		NameValue[] headers = res.getHeaders();
//		for(NameValue header : headers)
//		{
//			String name = header.getName();
//			if("set-cookie".equalsIgnoreCase(name))
//			{
//				String value = header.getValue();
//				Set<Cookie> cookies = decoder.decode(value);
//				for(Cookie cookie : cookies)
//				{
//					cookieByName.put(cookie.getName(), cookie);
//				}
//			}
//		}
//
//		Set<Cookie> decoded = new TreeSet<>();
//		for(String name : cookieByName.keySet())
//		{
//			decoded.add(cookieByName.get(name));
//		}
//		
//		return new CookiesImpl(decoded);
//	}

	public static Cookie replaceCookie(HttpResponse resp, Cookie old)
	{
		String name = old.getName();
		Cookie cookie = getCookies(resp).byName(name);
		if(cookie == null)
		{
			return old;
		}
		return cookie;
	}

	public static String getCookieNameAndValue(Cookie cookie)
	{
		return cookie.getName() + "=" + cookie.getValue();
	}

	public static Cookies mergeCookies(Cookies first, Cookies second)
	{
		Cookies     result    = new CookiesImpl();
		Set<Cookie> merged    = result.getBuffer();
		Set<Cookie> firstSet  = first.getBuffer();
		Set<Cookie> secondSet = second.getBuffer();

		merged.addAll(firstSet);
		
		for(Cookie onSecond : secondSet)
		{
			String name    = onSecond.getName();
			Cookie onFirst = result.byName(name);
			if(onFirst != null)
			{
				merged.remove(onFirst);
			}
			merged.add(onSecond);
		}
		return new CookiesImpl(merged);
	}
	
	public static Cookies mergeCookies(Cookies first, HttpResponse res)
	{
		Map<String,  Cookie>        newCookies = new HashMap<>();
		List<String> expiredCookies            = new ArrayList<>();

		NameValue[] headers = res.getHeaders();
		for(NameValue header : headers)
		{
			String name = header.getName();
			if("set-cookie".equalsIgnoreCase(name))
			{				
				String value = header.getValue();
				System.err.println(value);
				
				if(value.contains("Expires"))
				{
					expiredCookies.add(value.split("=")[0]);
				}
				else
				{
					Set<Cookie> cookies = decoder.decode(value);
					for(Cookie cookie : cookies)
					{
						newCookies.put(cookie.getName(), cookie);
					}
				}
			}
		}

		Set<Cookie> newSet = new TreeSet<>();
		for(String name : newCookies.keySet())
		{
			newSet.add(newCookies.get(name));
		}
		
		Cookies     result    = new CookiesImpl();
		Set<Cookie> merged    = result.getBuffer();
		Set<Cookie> firstSet  = first.getBuffer();

		merged.addAll(firstSet);
		
		for(Cookie onSecond : newSet)
		{
			String name    = onSecond.getName();
			Cookie onFirst = result.byName(name);
			if(onFirst != null)
			{
				merged.remove(onFirst);
			}
			merged.add(onSecond);
		}
		
		for(String s : expiredCookies)
		{
			Cookie target = null;
			
			for(Cookie c : merged)
			{
				if(c.getName().startsWith(s))
				{
					target = c;
					break;
				}
			}
			
			if(target != null)
			{
				merged.remove(target);
			}
		}
		
		return new CookiesImpl(merged);
	}
}