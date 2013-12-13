package xingu.http.client;

import java.util.List;
import java.util.Set;

import org.jboss.netty.handler.codec.http.Cookie;

public interface Cookies
{
	Cookie byName(String name);
	
	List<String> names();
	
	Set<Cookie> set();
}
