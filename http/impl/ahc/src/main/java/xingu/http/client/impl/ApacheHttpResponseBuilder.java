package xingu.http.client.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpUriRequest;

import xingu.http.client.NameValue;
import xingu.http.client.HttpResponse;
import xingu.http.client.impl.NameValueImpl;

public class ApacheHttpResponseBuilder
{

	public static <T> HttpResponse<T> build(HttpUriRequest req, org.apache.http.HttpResponse res, Class<T> target)
		throws IOException
	
	{
		NameValue[]    headers     = getHeaders(res);
		int         code        = res.getStatusLine().getStatusCode();

		T           body;
		HttpEntity  entity      = res.getEntity();
		InputStream is          = entity.getContent();
		byte[]      raw         = IOUtils.toByteArray(is);
		InputStream replacement = new BufferedInputStream(new ByteArrayInputStream(raw));

		if (String.class.equals(target))
		{
			body = (T) new String(raw);
		}
		else if (InputStream.class.equals(target))
		{
			body = (T) replacement;
		}
		else
		{
			throw new IOException("Unknown result type. Only String and InputStream are supported.");
		}

		
		ApacheHttpResponse<T> result = new ApacheHttpResponse<T>(req, res);
		result.setCode(code);
		result.setHeaders(headers);
		result.setBody(body);
		result.setRawBody(replacement);
		return result;
	}

	private static NameValue[] getHeaders(org.apache.http.HttpResponse response)
	{
		org.apache.http.Header[] allHeaders = response.getAllHeaders();
		NameValue[] result = new NameValue[allHeaders.length];
		for(int i = 0; i < result.length; i++)
		{
			org.apache.http.Header h = allHeaders[i];
			result[i] = new NameValueImpl(h.getName().toLowerCase(), h.getValue());
		}
		return result;
	}
}
