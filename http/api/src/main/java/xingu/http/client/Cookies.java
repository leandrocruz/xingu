package xingu.http.client;

import java.util.Set;

import org.jboss.netty.handler.codec.http.Cookie;

public interface Cookies
{
	Cookie byName(String name);
	
	Set<Cookie> set();
}
