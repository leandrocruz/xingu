package xingu.http.client.mock;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

import org.hamcrest.Matcher;
import org.jboss.netty.handler.codec.http.Cookie;

import xingu.http.client.HttpClient;
import xingu.http.client.HttpRequest;
import xingu.http.client.HttpResponse;
import xingu.http.client.impl.CommandLineBuilder;

public class HttpMockerOld
{
	private  HttpRequest			req;

	private  CommandLineBuilder	builder;

	private Matcher<String>				matcher;

	private HttpClient	http;

	public HttpMockerOld(String uri)
	{
		this(null, uri, new ExactUrlMatcher(uri));
	}

	public HttpMockerOld(CommandLineBuilder builder, String uri)
	{
		this(builder, uri, new ExactUrlMatcher(uri));
	}

	public HttpMockerOld(CommandLineBuilder builder, String uri, Matcher<String> matcher)
	{
		this.builder = builder;
		this.matcher = matcher;
		req = mock(HttpRequest.class);
		when(req.getUri()).thenReturn(uri);
		when(req.ndc(any(String.class))).thenReturn(req);
		when(req.withCookie(any(Cookie.class))).thenReturn(req);
		when(req.field(any(String.class), any(String.class))).thenReturn(req);
		when(req.header(any(String.class), any(String.class))).thenReturn(req);
		when(req.withUserAgent(any(String.class))).thenReturn(req);
	}

	public HttpMockerOld(HttpClient http)
	{
		this(http, null);
	}

	public HttpMockerOld(HttpClient http, CommandLineBuilder builder)
	{
		this.http    = http;
		this.builder = builder;
	}

	public HttpRequest get()
	{
		return req;
	}

	public HttpMockerOld to(HttpResponse res)
	{
		when(req.exec()).thenReturn(res);
		return this;
	}

	public HttpMockerOld to(String file)
		throws Exception
	{
		File f = getFile(file);
		if(f == null)
		{
			return this;
		}
		
		HttpResponse res = builder.responseFrom(req, f);
		when(req.exec()).thenReturn(res);
		return this;
	}

	public HttpMockerOld doGet(HttpClient http)
	{
		when(http.get(argThat(matcher))).thenReturn(req);
		return this;
	}

	public HttpMockerOld doPost(HttpClient http)
	{
		when(http.post(argThat(matcher))).thenReturn(req);
		return this;
	}

    protected File getFile(String name)
    {
    	URL url = getResource(name);
    	if(url == null)
    	{
    		System.err.println("File '" + name + "' not found");
    		return null;
    	}
    	return new File(url.getFile());
    }
    
	protected URL getResource(String name)
	{
		return Thread.currentThread().getContextClassLoader().getResource(name);
	}

	public static HttpResponse asResponse(File file)
		throws Exception
	{
		InputStream  is  = new FileInputStream(file);
		HttpResponse res = mock(HttpResponse.class);
		when(res.getRawBody()).thenReturn(is);
		return res;
	}

	/* new */
	
	public HttpMockerOld get(String uri)
	{
		return get(new ExactUrlMatcher(uri));
	}
	
	public HttpMockerOld get(UrlMatcher matcher)
	{
		String uri = matcher.getUri();

		req = mock(HttpRequest.class);
		when(req.getUri()).thenReturn(uri);
		when(req.ndc(any(String.class))).thenReturn(req);
		when(req.withCookie(any(Cookie.class))).thenReturn(req);
		when(req.field(any(String.class), any(String.class))).thenReturn(req);
		when(req.header(any(String.class), any(String.class))).thenReturn(req);
		when(req.withUserAgent(any(String.class))).thenReturn(req);

		return this;
	}

	public void to(File file)
		throws Exception
	{
		HttpResponse res = asResponse(file);
		when(req.exec()).thenReturn(res);
	}
}