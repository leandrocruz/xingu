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
import org.mockito.internal.matchers.Any;

import xingu.http.client.Attachment;
import xingu.http.client.Cookies;
import xingu.http.client.HttpClient;
import xingu.http.client.HttpContext;
import xingu.http.client.HttpProgressListener;
import xingu.http.client.HttpRequest;
import xingu.http.client.HttpResponse;
import xingu.http.client.ResponseInspector;
import xingu.http.client.impl.curl.CurlResponseParser;

public class HttpMocker
{
	private HttpRequest			req;

	private HttpClient			http;

	public HttpMocker(HttpClient http)
	{
		this.http = http;
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
		when(req.context(any(HttpContext.class))).thenReturn(req);
		when(req.withCookie(any(Cookie.class))).thenReturn(req);
		when(req.withCookies(any(Cookies.class))).thenReturn(req);
		when(req.field(any(String.class), any(String.class))).thenReturn(req);
		when(req.field(any(String.class), any(String.class), any(String.class))).thenReturn(req);
		when(req.field(any(String.class), any(Integer.class))).thenReturn(req);
		when(req.field(any(String.class), any(Long.class))).thenReturn(req);
		when(req.field(any(String.class))).thenReturn(req);
		when(req.header(any(String.class), any(String.class))).thenReturn(req);
		when(req.expects(any(Integer.class))).thenReturn(req);
		when(req.expects(any(Integer.class), any(String.class))).thenReturn(req);
		when(req.expects(any(ResponseInspector.class))).thenReturn(req);
		when(req.withAttachment(any(Attachment.class))).thenReturn(req);
		when(req.multipart(any(Boolean.class))).thenReturn(req);
		when(req.withUserAgent(any(String.class))).thenReturn(req);
		when(req.name(any(String.class))).thenReturn(req);
		when(req.listener(any(HttpProgressListener.class))).thenReturn(req);
		when(req.ignoreSSLCertificates(any(Boolean.class))).thenReturn(req);
		when(req.withKeepAlive(any(String.class))).thenReturn(req);
		when(req.payload(any(String.class))).thenReturn(req);
		return req;
	}

	public HttpMocker to(HttpResponse res)
	{
		when(req.exec()).thenReturn(res);
		when(req.execAndRetry(any(int.class))).thenReturn(res);
		return this;
	}

	public void toBody(File file)
		throws Exception
	{
		HttpResponse res = asResponse(file);
		when(req.exec()).thenReturn(res);
		when(req.execAndRetry(any(int.class))).thenReturn(res);
	}

	public HttpMocker to(File file)
		throws Exception
	{
		HttpResponse res = CurlResponseParser.responseFrom(req.getUri(), file);
		when(req.exec()).thenReturn(res);
		when(req.execAndRetry(any(int.class))).thenReturn(res);
		return this;
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
		when(req.execAndRetry(any(int.class))).thenReturn(res, array);
		return this;

	}

	private HttpResponse toResponse(String file)
		throws Exception
	{
		File f = getFile(file);
		return CurlResponseParser.responseFrom(req.getUri(), f);
	}
	
	public HttpMocker to(String file)
		throws Exception
	{
		File f = getFile(file);
		if(f == null)
		{
			return this;
		}

		HttpResponse res = CurlResponseParser.responseFrom(req.getUri(), f);
		when(req.exec()).thenReturn(res);
		when(req.execAndRetry(any(int.class))).thenReturn(res);
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