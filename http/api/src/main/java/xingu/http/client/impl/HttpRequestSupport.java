package xingu.http.client.impl;

import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.handler.codec.http.Cookie;

import xingu.http.client.Cookies;
import xingu.http.client.HttpException;
import xingu.http.client.HttpRequest;
import xingu.http.client.NameValue;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.StringUtils;

public abstract class HttpRequestSupport
	implements HttpRequest
{
	protected String			method;

	protected String			uri;

	protected String			ua;

	protected String			certificate;

	protected String			certificatePassword;

	protected String			authUser;

	protected String			authPassword;

	protected boolean			multipart;

	protected String			ndc;

	protected int				expectedCode;

	protected String			messageIfCodeMismatch;

	protected Cookies			cookies		= new CookiesImpl();

	protected List<NameValue>	fields		= new ArrayList<NameValue>();

	protected List<NameValue>	attachments	= new ArrayList<NameValue>();

	protected List<NameValue>	headers		= new ArrayList<NameValue>();

	public HttpRequestSupport(String uri, String method)
	{
		this.uri    = uri;
		this.method = method;

	}

	@Override
	public String getUri()
	{
		return uri;
	}

	@Override
	public String getMethod()
	{
		return method;
	}

	@Override
	public boolean isPost()
	{
		return "POST".equalsIgnoreCase(method);
	}

	@Override
	public HttpRequest header(String name, String value)
	{
		headers.add(new NameValueImpl(name, value));
		return this;
	}

	@Override
	public HttpRequest field(String name, String value)
	{
		if(name == null)
		{
			throw new NotImplementedYet("Name cannot be null.");
		}
		if(value == null)
		{
			throw new NotImplementedYet("Value cannot be null.");
		}
		fields.add(new NameValueImpl(name, value));
		return this;
	}

	@Override
	public HttpRequest withCertificate(String certificate)
	{
		this.certificate = certificate;
		return this;
	}

	@Override
	public HttpRequest withCookie(Cookie cookie)
	{
		if(cookie == null)
		{
			throw new NotImplementedYet("Cookie is null");
		}
		cookies.add(cookie);
		return this;
	}

	@Override
	public HttpRequest withCookies(Cookies cookies)
	{
		this.cookies = cookies;
		return this;
	}
	
	@Override
	public String getCertificate()
	{
		return certificate;
	}

	@Override
	public List<NameValue> getFields()
	{
		return fields;
	}

	@Override
	public List<NameValue> getHeaders()
	{
		return headers;
	}

	@Override
	public List<NameValue> getAttachments()
	{
		return attachments;
	}

	@Override
	public Cookies getCookies()
	{
		return cookies;
	}

	@Override
	public HttpRequest withCertificate(String certificate, String password)
	{
		this.certificatePassword = password;
		return withCertificate(certificate);
	}

	@Override
	public String getCertificatePassword()
	{
		return certificatePassword;
	}

	@Override
	public HttpRequest withUserAgent(String ua)
	{
		this.ua = ua;
		return this;
	}

	public HttpRequest multipart(boolean multipart)
	{
		this.multipart = multipart;
		return this;
	}

	@Override
	public HttpRequest withAttachment(String name, String filePath)
	{
		attachments.add(new NameValueImpl(name, filePath));
		return this;
	}

	@Override
	public HttpRequest withAuthentication(String user, String password)
	{
		this.authUser 		= user;
		this.authPassword 	= password;
		return this;
	}

	@Override
	public String getAuthenticationUser()
	{
		return authUser;
	}

	@Override
	public String getAuthenticationPassword()
	{
		return authPassword;
	}

	@Override
	public String getUserAgent()
	{
		return ua;
	}

	public boolean isMultipart()
	{
		return multipart;
	}

	@Override
	public HttpRequest ndc(String ndc)
	{
		this.ndc = ndc;
		return this;
	}

	//@Override
	public HttpRequest queryString(String name, String value)
	{
		throw new NotImplementedYet();
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

	@Override
	public HttpRequest expects(int code)
	{
		this.expectedCode = code;
		return this;
	}
	
	@Override
	public HttpRequest expects(int code, String errorMessage)
	{
		this.expectedCode = code;
		this.messageIfCodeMismatch = errorMessage;
		return this;
	}

	protected void checkCode(int code)
	{
		if(expectedCode > 0 && code != expectedCode)
		{
			if(messageIfCodeMismatch != null)
			{
				throw new HttpException(messageIfCodeMismatch);
			}
			throw new HttpException("Expected response code mismatch: " + expectedCode + " != " + code);
		}
	}

	@Override
	public String toString()
	{
		return method + " " + uri;
	}
}