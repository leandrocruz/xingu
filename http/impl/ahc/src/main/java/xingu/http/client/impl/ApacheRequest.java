package xingu.http.client.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jboss.netty.handler.codec.http.Cookie;

import xingu.http.client.CookieUtils;
import xingu.http.client.HttpException;
import xingu.http.client.HttpRequest;
import xingu.http.client.HttpResponse;
import xingu.http.client.NameValue;

public class ApacheRequest
	extends HttpRequestSupport
{
	private HttpUriRequest		req;

	private List<NameValuePair>	params	= new ArrayList<NameValuePair>();

	private List<NameValuePair>	files	= new ArrayList<NameValuePair>();

	private List<Cookie>		cookies	= new ArrayList<Cookie>();

	private boolean				isMultipart;

	public ApacheRequest(HttpUriRequest req)
	{
		this.req = req;
	}

	@Override
	public HttpRequest header(String name, String value)
	{
		req.addHeader(name, value);
		return this;
	}

	@Override
	public HttpRequest field(String name, String value)
	{
		params.add(new BasicNameValuePair(name, value));
		return this;
	}

	@Override
	public String getUri()
	{
		return req.getURI().toString();
	}

	@Override
	public List<NameValue> getFields()
	{
		return null;
	}

	@Override
	public HttpRequest multipart(boolean isMultipartFormData)
	{
		this.isMultipart = isMultipartFormData;
		return this;
	}

	@Override
	public HttpRequest upload(String name, String filePath)
	{
		files.add(new BasicNameValuePair(name, filePath));
		return this;
	}

	@Override
	public HttpRequest withCookie(Cookie cookie)
	{
		cookies.add(cookie);
		return this;
	}

	@Override
	public String toString()
	{
		return req.getMethod() + " " + req.getURI();
	}

	@Override
	public HttpResponse exec()
		throws HttpException
	{
		String method = req.getMethod();
		if("POST".equals(method))
		{
			HttpPost   post   = ((HttpPost) req);
			HttpEntity entity = null;
			if(isMultipart)
			{
				MultipartEntityBuilder builder = MultipartEntityBuilder.create();
				for(NameValuePair pair : files)
				{
					String name = pair.getName();
					String path = pair.getValue();
					builder.addPart(name, new FileBody(new File(path)));
				}
				entity = builder.build();
			}
			else
			{
				entity = new UrlEncodedFormEntity(params, Consts.UTF_8);
			}
			
			post.setEntity(entity);
		}
		
		for(Cookie cookie : cookies)
		{
			String cookieNameAndValue = CookieUtils.getCookieNameAndValue(cookie);
			req.addHeader("Cookie", cookieNameAndValue);
		}
		
		CloseableHttpClient client = HttpClients.createDefault();
		try
		{
			org.apache.http.HttpResponse res = client.execute(req);
			return ApacheHttpResponseBuilder.build(req, res);
		}
		catch(Exception e)
		{
			throw new HttpException(e);
		}
		finally
		{
			try
			{
				client.close();
			}
			catch(IOException e)
			{
				throw new HttpException(e);
			}
		}
	}
}