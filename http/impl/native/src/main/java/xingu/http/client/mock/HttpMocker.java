package xingu.http.client.mock;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import org.jboss.netty.handler.codec.http.Cookie;

import xingu.http.client.HttpClient;
import xingu.http.client.HttpRequest;
import xingu.http.client.HttpResponse;
import xingu.http.client.impl.CommandLineBuilder;

public class HttpMocker
{
	private HttpRequest			req;

	private HttpClient			http;

	private CommandLineBuilder	builder;

	public HttpMocker(HttpClient http)
	{
		this(http, null);
	}

	public HttpMocker(HttpClient http, CommandLineBuilder builder)
	{
		this.http    = http;
		this.builder = builder;
	}

	public HttpMocker get(String uri)
	{
		return get(new ExactUrlMatcher(uri));
	}
	
	public HttpMocker get(UrlMatcher matcher)
	{
		req = mockRequest(matcher);
		when(http.get(argThat(matcher))).thenReturn(req);
		return this;
	}

	public HttpMocker post(String uri)
	{
		return post(new ExactUrlMatcher(uri));
	}

	public HttpMocker post(UrlMatcher matcher)
	{
		req = mockRequest(matcher);
		when(http.post(argThat(matcher))).thenReturn(req);
		return this;
	}

	private HttpRequest mockRequest(UrlMatcher matcher)
	{
		String      uri = matcher.getUri();
		HttpRequest req = mock(HttpRequest.class);

		when(req.getUri()).thenReturn(uri);
		when(req.ndc(any(String.class))).thenReturn(req);
		when(req.withCookie(any(Cookie.class))).thenReturn(req);
		when(req.field(any(String.class), any(String.class))).thenReturn(req);
		when(req.field(any(String.class), any(Integer.class))).thenReturn(req);
		when(req.field(any(String.class), any(Long.class))).thenReturn(req);
		when(req.field(any(String.class))).thenReturn(req);
		when(req.header(any(String.class), any(String.class))).thenReturn(req);
		when(req.withUserAgent(any(String.class))).thenReturn(req);
		
		return req;
	}

	public HttpMocker to(HttpResponse res)
	{
		when(req.exec()).thenReturn(res);
		return this;
	}

	public void to(File file)
		throws Exception
	{
		HttpResponse res = asResponse(file);
		when(req.exec()).thenReturn(res);
	}

	public HttpMocker to(String... files)
		throws Exception
	{
		HttpResponse res = toResponse(files[0]);
		HttpResponse[] array = new HttpResponse[files.length];
		for(int i = 1; i < files.length; i++)
		{
			String     file = files[i];
			array[i-1]      = toResponse(file);
		}

		when(req.exec()).thenReturn(res, array);
		return this;

	}

	private HttpResponse toResponse(String file)
		throws Exception
	{
		File f = getFile(file);
		return builder.responseFrom(req, f);
	}
	
	public HttpMocker to(String file)
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

	public static HttpResponse asResponse(File file)
		throws Exception
	{
		InputStream is = new FileInputStream(file);
		HttpResponse res = mock(HttpResponse.class);
		when(res.getRawBody()).thenReturn(is);
		return res;
	}

	protected File getFile(String name)
	{
		URL url = getResource(name);
		if(url == null)
		{
			return null;
		}
		return new File(url.getFile());
	}

	protected URL getResource(String name)
	{
		return Thread.currentThread().getContextClassLoader().getResource(name);
	}
}