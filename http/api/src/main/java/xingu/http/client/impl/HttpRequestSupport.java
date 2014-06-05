package xingu.http.client.impl;

import java.util.List;

import org.jboss.netty.handler.codec.http.Cookie;
import org.jboss.netty.handler.codec.http.HttpHeaders;

import xingu.http.client.Cookies;
import xingu.http.client.HttpException;
import xingu.http.client.HttpRequest;
import xingu.http.client.HttpResponse;
import xingu.http.client.NameValue;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.StringUtils;

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
		this.header(HttpHeaders.Names.USER_AGENT, ua);
		return this;
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
	
	
	@Override
	public HttpRequest field(String nameValue)
	{
		String[] parts = nameValue.split(":");
		return field(parts[0], parts.length == 2 ? parts[1] : StringUtils.EMPTY);
	}

	@Override
	public HttpRequest field(String name, int value)
	{
		return field(name, String.valueOf(value));
	}

	@Override
	public HttpRequest field(String name, long value)
	{
		return field(name, String.valueOf(value));
	}
}