package xingu.http.client.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jboss.netty.handler.codec.http.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	protected String				certificate;

	protected List<Cookie>			cookies	= new ArrayList<Cookie>();

	protected List<NameValue>		fields	= new ArrayList<NameValue>();

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
		try
		{
			File   file = File.createTempFile("curl-http-response-", ".html");
			String cmd  = builder.buildLine(this, file);
			logger.debug("Executing command: {}", cmd);
			
			int result = pm.exec(cmd);
			if(result != 0)
			{
				throw new NotImplementedYet("ERROR executing wget processes: " + result);
			}
			
			return builder.responseFrom(this, file);
		}
		catch(Exception e)
		{
			throw new HttpException(e);
		}
	}
}
