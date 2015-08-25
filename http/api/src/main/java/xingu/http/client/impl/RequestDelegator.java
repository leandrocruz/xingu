package xingu.http.client.impl;

import java.util.List;

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
		return req.context(ctx);
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
		return req.header(name, value);
	}

	@Override
	public List<NameValue> getFields()
	{
		return req.getFields();
	}

	@Override
	public HttpRequest field(String nameValue)
	{
		return req.field(nameValue);
	}

	@Override
	public HttpRequest field(String name, String value)
	{
		return req.field(name, value);
	}

	@Override
	public HttpRequest field(String name, String value, String type)
	{
		return req.field(name, value, type);
	}

	@Override
	public HttpRequest field(String name, int value)
	{
		return req.field(name, value);
	}

	@Override
	public HttpRequest field(String name, long value)
	{
		return req.field(name, value);
	}

	@Override
	public Cookies getCookies()
	{
		return req.getCookies();
	}

	@Override
	public HttpRequest withCookie(Cookie cookie)
	{
		return req.withCookie(cookie);
	}

	@Override
	public HttpRequest withCookies(Cookies cookies)
	{
		return req.withCookies(cookies);
	}

	@Override
	public HttpRequest multipart(boolean isMultipartFormData)
	{
		return req.multipart(isMultipartFormData);
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
		return req.withCertificate(certificate);
	}

	@Override
	public HttpRequest withCertificate(String certificate, String password)
	{
		return req.withCertificate(certificate, password);
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
		return req.withUserAgent(ua);
	}

	@Override
	public List<Attachment> getAttachments()
	{
		return req.getAttachments();
	}

	@Override
	public HttpRequest withAttachment(Attachment attachment)
	{
		return req.withAttachment(attachment);
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
		return req.withAuthentication(user, password);
	}

	@Override
	public HttpRequest expects(int code)
	{
		return req.expects(code);
	}

	@Override
	public HttpRequest expects(int code, String messageIfError)
	{
		return req.expects(code, messageIfError);
	}

	@Override
	public HttpRequest expects(ResponseInspector inspector)
	{
		return req.expects(inspector);
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
		return req.name(name);
	}

	@Override
	public HttpRequest ignoreSSLCertificates(boolean ignore)
	{
		return req.ignoreSSLCertificates(ignore);
	}

	@Override
	public boolean ignoreSSLCertificates()
	{
		return req.ignoreSSLCertificates();
	}

	@Override
	public HttpRequest listener(HttpProgressListener listener)
	{
		return req.listener(listener);
	}

	@Override
	public HttpRequest payload(String payload)
	{
		return req.payload(payload);
	}

	@Override
	public String getPayload()
	{
		return req.getPayload();
	}
}