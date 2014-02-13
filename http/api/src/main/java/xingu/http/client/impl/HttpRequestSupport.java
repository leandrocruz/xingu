package xingu.http.client.impl;

import java.util.List;

import org.jboss.netty.handler.codec.http.Cookie;

import xingu.http.client.Cookies;
import xingu.http.client.HttpException;
import xingu.http.client.HttpRequest;
import xingu.http.client.HttpResponse;
import xingu.http.client.NameValue;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public abstract class HttpRequestSupport
	implements HttpRequest
{
	@Override
	public String getAuthenticationUser()
	{
		throw new NotImplementedYet();
	}

	@Override
	public String getAuthenticationPassword()
	{
		throw new NotImplementedYet();
	}
	
	@Override
	public HttpRequest withAuthentication(String user, String password)
	{
		throw new NotImplementedYet();
	}
	
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
	public boolean isMultipart()
	{
		throw new NotImplementedYet();
	}

	@Override
	public HttpRequest queryString(String name, String filePath)
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
	
	public HttpRequest multipart(boolean isMultipartFormData)
	{
		throw new NotImplementedYet();
	}

	@Override
	public HttpRequest upload(String name, String filePath)
	{
		throw new NotImplementedYet();
	}
	
	@Override
	public List<NameValue> getUploadFiles()
	{
		throw new NotImplementedYet();
	}

	@Override
	public HttpResponse exec()
		throws HttpException
	{
		throw new NotImplementedYet();
	}

	@Override
	public HttpRequest ndc(String ndc)
	{
		return this;
	}
}