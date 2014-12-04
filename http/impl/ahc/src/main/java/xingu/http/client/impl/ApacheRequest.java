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

import xingu.http.client.Attachment;
import xingu.http.client.CookieUtils;
import xingu.http.client.HttpException;
import xingu.http.client.HttpResponse;
import xingu.http.client.NameValue;

public class ApacheRequest
	extends HttpRequestSupport
{
	private HttpUriRequest		req;

	public ApacheRequest(HttpUriRequest req)
	{
		super(req.getURI().toString(), req.getMethod());
		this.req = req;
	}

	@Override
	public HttpResponse exec()
		throws HttpException
	{
		for(NameValue nv : headers)
		{
			req.addHeader(nv.getName(), nv.getValue());
		}
		
		boolean isPost = isPost();
		if(isPost)
		{
			HttpPost   post   = ((HttpPost) req);
			HttpEntity entity = null;
			if(multipart)
			{
				MultipartEntityBuilder builder = MultipartEntityBuilder.create();
				for(Attachment attachment : attachments)
				{
					String name = attachment.getName();
					File   file = attachment.getFile();
					builder.addPart(name, new FileBody(file));
					
				}
				entity = builder.build();
			}
			else
			{
				List<NameValuePair>	params	= new ArrayList<NameValuePair>();
				for(NameValue nv : fields)
				{
					params.add(new BasicNameValuePair(nv.getName(), nv.getValue()));
				}
				entity = new UrlEncodedFormEntity(params, Consts.UTF_8);
			}
			
			post.setEntity(entity);
		}
		
		for(Cookie cookie : cookies.getBuffer())
		{
			String cookieNameAndValue = CookieUtils.getCookieNameAndValue(cookie);
			req.addHeader("Cookie", cookieNameAndValue);
		}
		
		CloseableHttpClient client = HttpClients.createDefault();
		try
		{
			org.apache.http.HttpResponse res = client.execute(req);
			HttpResponse build = ApacheHttpResponseBuilder.build(req, res);
			check(build);
			return build;
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