package xingu.http.client.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpUriRequest;

import xingu.http.client.HttpResponse;
import xingu.http.client.NameValue;

public class ApacheHttpResponseBuilder
{
	public static HttpResponse build(HttpUriRequest req, org.apache.http.HttpResponse res)
		throws IOException
	
	{
		NameValue[] headers     = getHeaders(res);
		int         code        = res.getStatusLine().getStatusCode();

		HttpEntity  entity      = res.getEntity();
		InputStream is          = entity.getContent();
		
		/* We need to read and replace the stream while the socket is still open */
		byte[]      raw         = IOUtils.toByteArray(is);
		InputStream replacement = new BufferedInputStream(new ByteArrayInputStream(raw));

		
		HttpResponseImpl result = new HttpResponseImpl();
		result.setUri(req.getURI().toString());
		result.setCode(code);
		result.setHeaders(headers);
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
