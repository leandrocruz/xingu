package xingu.http.client.impl;

import java.util.List;

import org.jboss.netty.handler.codec.http.Cookie;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import xingu.http.client.Cookies;
import xingu.http.client.HttpRequest;
import xingu.http.client.NameValue;

public abstract class HttpRequestSupport
	implements HttpRequest
{
	@Override
	public HttpRequest withCertificate(String certificate)
	{
		throw new NotImplementedYet();
	}
	
	@Override
	public HttpRequest withCertificate(String certificate, String password)
	{
		throw new NotImplementedYet();
	}

	@Override
	public HttpRequest withCookie(Cookie cookie)
	{
		throw new NotImplementedYet();
	}
	
	@Override
	public HttpRequest withCookies(Cookies cookie)
	{
		throw new NotImplementedYet();
	}
	
	@Override
	public HttpRequest withUserAgent(String ua)
	{
		return null;
	}

	@Override
	public String getCertificate()
	{
		throw new NotImplementedYet();
	}

	@Override
	public List<NameValue> getFields()
	{
		throw new NotImplementedYet();
	}
	
	@Override
	public List<NameValue> getHeaders()
	{
		throw new NotImplementedYet();
	}

	@Override
	public List<Cookie> getCookies()
	{
		throw new NotImplementedYet();
	}

	@Override
	public boolean isPost()
	{
		throw new NotImplementedYet();
	}
	
	@Override
	public String getCertificatePassword()
	{
		throw new NotImplementedYet();
	}

	@Override
	public String getUserAgent()
	{
		throw new NotImplementedYet();
	}
}