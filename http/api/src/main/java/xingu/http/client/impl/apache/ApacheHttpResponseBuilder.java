package xingu.http.client.impl.apache;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpUriRequest;

import xingu.http.client.Header;
import xingu.http.client.HttpResponse;
import xingu.http.client.impl.HeaderImpl;

public class ApacheHttpResponseBuilder
{

	public static <T> HttpResponse<T> build(HttpUriRequest req, org.apache.http.HttpResponse res, Class<T> target)
		throws IOException
	
	{
		Header[]    headers = getHeaders(res);
		int         code    = res.getStatusLine().getStatusCode();

		HttpEntity  entity  = res.getEntity();
		InputStream is      = entity.getContent();
		T body;
		if (String.class.equals(target))
		{
			byte[] raw = IOUtils.toByteArray(is);
			body = (T) new String(raw);
		}
		else if (InputStream.class.equals(target))
		{
			body = (T) is;
		}
		else
		{
			throw new IOException("Unknown result type. Only String and InputStream are supported.");
		}

		
		ApacheHttpResponse<T> result = new ApacheHttpResponse<T>(req, res);
		result.setCode(code);
		result.setHeaders(headers);
		result.setBody(body);
		result.setRawBody(is);
		return result;
	}

	private static Header[] getHeaders(org.apache.http.HttpResponse response)
	{
		org.apache.http.Header[] allHeaders = response.getAllHeaders();
		Header[] result = new Header[allHeaders.length];
		for(int i = 0; i < result.length; i++)
		{
			org.apache.http.Header h = allHeaders[i];
			result[i] = new HeaderImpl(h.getName().toLowerCase(), h.getValue());
		}
		return result;
	}
}
