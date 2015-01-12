package xingu.http.client.impl;

import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.handler.codec.http.Cookie;

import xingu.http.client.Attachment;
import xingu.http.client.Cookies;
import xingu.http.client.HttpContext;
import xingu.http.client.HttpRequest;
import xingu.http.client.HttpResponse;
import xingu.http.client.NameValue;
import xingu.http.client.ResponseInspector;
import xingu.lang.NotImplementedYet;
import xingu.netty.http.HttpUtils;
import xingu.utils.StringUtils;

public abstract class HttpRequestSupport
	implements HttpRequest
{
	protected HttpContext			context;

	protected String				name;

	protected String				method;

	protected String				uri;

	protected String				ua;

	protected String				charset;

	protected String				certificate;

	protected String				certificatePassword;

	protected String				authUser;

	protected String				authPassword;

	protected boolean				multipart;

	protected Cookies				cookies		= new CookiesImpl();

	protected List<NameValue>		fields		= new ArrayList<>();

	protected List<Attachment>		attachments	= new ArrayList<>();

	protected List<NameValue>		headers		= new ArrayList<>();

	private List<ResponseInspector>	inspectors	= new ArrayList<>();

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
		return field(name, value, null);
	}

	@Override
	public HttpRequest field(String name, String value, String type)
	{
		if(name == null)
		{
			throw new NotImplementedYet("Name cannot be null.");
		}
		if(value == null)
		{
			value = "";
		}
		fields.add(new NameValueImpl(name, value, type));
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
	public List<Attachment> getAttachments()
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
	public HttpRequest withAttachment(Attachment attachment)
	{
		attachments.add(attachment);
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
	public HttpRequest context(HttpContext ctx)
	{
		this.context = ctx;
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
		ResponseCodeInspector inspector = ResponseCodeInspector.forCode(code, null);
		return expects(inspector);
	}
	
	@Override
	public HttpRequest expects(int code, String messageIfError)
	{
		ResponseCodeInspector inspector = ResponseCodeInspector.forCode(code, messageIfError);
		return expects(inspector);
	}

	@Override
	public HttpRequest expects(ResponseInspector inspector)
	{
		inspectors.add(inspector);
		return this;
	}

	protected void check(HttpResponse res)
		throws Exception
	{
		for(ResponseInspector inspector: inspectors)
		{
			inspector.throwErrorIf(res);
		}
	}

	@Override
	public void setCharset(String charset)
	{
		this.charset = charset;
	}

	@Override
	public String getCharset()
	{
		return charset != null ? charset : HttpUtils.DEFAULT_HTTP_CHARSET_NAME;
	}

	@Override
	public HttpRequest name(String name)
	{
		this.name = name;
		return this;
	}

	@Override
	public String toString()
	{
		return method + " " + uri;
	}
}