package xingu.http.client.impl.apache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import xingu.http.client.HttpException;
import xingu.http.client.HttpRequest;
import xingu.http.client.HttpResponse;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public class ApacheRequest
	implements HttpRequest
{
	private HttpUriRequest	req;
	
	private List<NameValuePair> params = new ArrayList<NameValuePair>();

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
	public HttpResponse<String> asString()
		throws HttpException
	{
		String method = req.getMethod();
		if("POST".equals(method))
		{
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, Consts.UTF_8);
			((HttpPost) req).setEntity(entity);
		}
		
		CloseableHttpClient client = HttpClients.createDefault();
		org.apache.http.HttpResponse res;
		try
		{
			res = client.execute(req);
			return ApacheHttpResponseBuilder.build(req, res, String.class);
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
				e.printStackTrace();
			}
		}
	}

	@Override
	public String getUri()
	{
		return req.getURI().toString();
	}

}