package xingu.http.client.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.handler.codec.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xingu.http.client.Cookies;
import xingu.http.client.HttpException;
import xingu.http.client.HttpRequest;
import xingu.http.client.HttpResponse;
import xingu.http.client.NameValue;
import xingu.process.ProcessManager;
import br.com.ibnetwork.xingu.container.Inject;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public class SimpleHttpRequest
	extends HttpRequestSupport
{
	@Inject
	protected ProcessManager		pm;

	@Inject
	protected CommandLineBuilder	builder;

	protected boolean				isPost;

	protected String				uri;

	private String					ua;

	protected String				certificate;

	private String					certificatePassword;

	protected List<Cookie>			cookies	= new ArrayList<Cookie>();

	protected List<NameValue>		fields	= new ArrayList<NameValue>();
	
	protected List<NameValue>		upload	= new ArrayList<NameValue>();
	
	protected List<NameValue>		headers	= new ArrayList<NameValue>();

	private boolean	multipart;

	protected static final Logger	logger	= LoggerFactory.getLogger(SimpleHttpRequest.class);

	public SimpleHttpRequest(String uri, boolean isPost)
	{
		this.uri = uri;
		this.isPost = isPost;
	}

	@Override
	public String getUri()
	{
		return uri;
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
		this.cookies.add(cookie);
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
	public List<NameValue> getUploadFiles()
	{
		return upload;
	}

	@Override
	public List<Cookie> getCookies()
	{
		return cookies;
	}

	@Override
	public boolean isPost()
	{
		return isPost;
	}
	
	@Override
	public HttpResponse<String> asString()
		throws HttpException
	{
		String impl = builder.name();
		try
		{
			File         file = File.createTempFile(impl + "-http-response-", ".html");
			List<String> cmd  = builder.buildLine(this, file);
			logger.info("Executing command: {}", StringUtils.join(cmd, " "));
			
			int result = pm.exec(cmd);
			if(result != 0)
			{
				throw new NotImplementedYet("ERROR executing "+impl+" processes: " + result);
			}
			
			return builder.responseFrom(this, file);
		}
		catch(Exception e)
		{
			throw new HttpException(e);
		}
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
	public HttpRequest withCookies(Cookies c)
	{
		cookies.addAll(c.set());
		return this;
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
	public HttpRequest upload(String name, String filePath)
	{
		upload.add(new NameValueImpl(name, filePath));
		return this;
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
	public HttpRequest queryString(String name, String filePath)
	{
		throw new NotImplementedYet();
	}
}