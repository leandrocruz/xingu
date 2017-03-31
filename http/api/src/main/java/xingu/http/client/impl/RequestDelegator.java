package xingu.http.client.impl;

import java.util.List;
import java.util.Map;

import org.jboss.netty.handler.codec.http.Cookie;

import xingu.http.client.Attachment;
import xingu.http.client.Cookies;
import xingu.http.client.HttpContext;
import xingu.http.client.HttpProgressListener;
import xingu.http.client.HttpRequest;
import xingu.http.client.NameValue;
import xingu.http.client.ResponseInspector;

public abstract class RequestDelegator
	implements HttpRequest
{
	protected HttpRequest req;
	
	public RequestDelegator(HttpRequest req)
	{
		this.req = req;
	}

	@Override
	public String getUri()
	{
		return req.getUri();
	}

	@Override
	public HttpRequest context(HttpContext ctx)
	{
		req.context(ctx);
		return this;
	}

	@Override
	public String getMethod()
	{
		return req.getMethod();
	}

	@Override
	public boolean isPost()
	{
		return req.isPost();
	}

	@Override
	public List<NameValue> getHeaders()
	{
		return req.getHeaders();
	}

	@Override
	public HttpRequest header(String name, String value)
	{
		req.header(name, value);
		return this;
	}

	@Override
	public List<NameValue> getFields()
	{
		return req.getFields();
	}

	@Override
	public HttpRequest field(String nameValue)
	{
		req.field(nameValue);
		return this;
	}

	@Override
	public HttpRequest field(String name, String value)
	{
		req.field(name, value);
		return this;
	}

	@Override
	public HttpRequest field(String name, String value, String type)
	{
		req.field(name, value, type);
		return this;
	}

	@Override
	public HttpRequest field(String name, int value)
	{
		req.field(name, value);
		return this;
	}

	@Override
	public HttpRequest field(String name, long value)
	{
		req.field(name, value);
		return this;
	}

	@Override
	public HttpRequest fields(Map<String, String> map)
	{
		req.fields(map);
		return this;
	}

	@Override
	public Cookies getCookies()
	{
		return req.getCookies();
	}

	@Override
	public HttpRequest withCookie(Cookie cookie)
	{
		req.withCookie(cookie);
		return this;
	}

	@Override
	public HttpRequest withCookies(Cookies cookies)
	{
		req.withCookies(cookies);
		return this;
	}

	@Override
	public HttpRequest multipart(boolean isMultipartFormData)
	{
		req.multipart(isMultipartFormData);
		return this;
	}

	@Override
	public boolean isMultipart()
	{
		return req.isMultipart();
	}

	@Override
	public String getCertificate()
	{
		return req.getCertificate();
	}

	@Override
	public HttpRequest withCertificate(String certificate)
	{
		req.withCertificate(certificate);
		return this;
	}

	@Override
	public HttpRequest withCertificate(String certificate, String password)
	{
		req.withCertificate(certificate, password);
		return this;
	}

	@Override
	public String getCertificatePassword()
	{
		return req.getCertificatePassword();
	}

	@Override
	public String getUserAgent()
	{
		return req.getUserAgent();
	}

	@Override
	public HttpRequest withUserAgent(String ua)
	{
		req.withUserAgent(ua);
		return this;
	}

	@Override
	public List<Attachment> getAttachments()
	{
		return req.getAttachments();
	}

	@Override
	public HttpRequest withAttachment(Attachment attachment)
	{
		req.withAttachment(attachment);
		return this;
	}

	@Override
	public String getAuthenticationUser()
	{
		return req.getAuthenticationUser();
	}

	@Override
	public String getAuthenticationPassword()
	{
		return req.getAuthenticationPassword();
	}

	@Override
	public HttpRequest withAuthentication(String user, String password)
	{
		req.withAuthentication(user, password);
		return this;
	}

	@Override
	public HttpRequest expects(int code)
	{
		req.expects(code);
		return this;
	}

	@Override
	public HttpRequest expects(int code, String messageIfError)
	{
		req.expects(code, messageIfError);
		return this;
	}

	@Override
	public HttpRequest expects(ResponseInspector inspector)
	{
		req.expects(inspector);
		return this;
	}

	@Override
	public void setCharset(String charset)
	{
		req.setCharset(charset);
	}

	@Override
	public String getCharset()
	{
		return req.getCharset();
	}

	@Override
	public HttpRequest name(String name)
	{
		req.name(name);
		return this;
	}

	@Override
	public HttpRequest ignoreSSLCertificates(boolean ignore)
	{
		req.ignoreSSLCertificates(ignore);
		return this;
	}

	@Override
	public boolean ignoreSSLCertificates()
	{
		return req.ignoreSSLCertificates();
	}

	@Override
	public HttpRequest listener(HttpProgressListener listener)
	{
		req.listener(listener);
		return this;
	}
	
	@Override
	public HttpRequest withKeepAlive(String seconds)
	{
		req.withKeepAlive(seconds);
		return this;
	}

	@Override
	public String getKeepAlive()
	{
		return req.getKeepAlive();
	}

	@Override
	public HttpRequest payload(String payload)
	{
		req.payload(payload);
		return this;
	}

	@Override
	public String getPayload()
	{
		return req.getPayload();
	}
	
	@Override
	public HttpRequest soap(boolean soap)
	{
		req.soap(soap);
		return this;
	}

	@Override
	public boolean isSoap()
	{
		return req.isSoap();
	}
	
	@Override
	public boolean sslAllowBeast()
	{
		return req.sslAllowBeast();
	}
	
	@Override
	public HttpRequest sslAllowBeast(boolean allow)
	{
		req.sslAllowBeast(allow);
		return this;
	}
	
	@Override
	public boolean sslV3()
	{
		return req.sslV3();
	}
	
	@Override
	public HttpRequest sslV3(boolean v3)
	{
		req.sslV3(v3);
		return this;
	}
}