package xingu.http.client.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;

import xingu.http.client.HttpProgressListener;
import xingu.http.client.HttpResponse;
import xingu.http.client.NameValue;
import xingu.lang.NotImplementedYet;

public class ApacheHttpResponseBuilder
{
	public static HttpResponse build(HttpUriRequest req, org.apache.http.HttpResponse res, HttpProgressListener listener)
		throws Exception
	{
		NameValue[] headers = getHeaders(res);
		int         code    = res.getStatusLine().getStatusCode();
		HttpEntity  entity  = res.getEntity();
		long        len     = entity.getContentLength();

		if(len > Integer.MAX_VALUE)
		{
			throw new NotImplementedYet("Sorry, but I can't create a buffer of size: " + len);
		}

		int           chunkSize;
		int           progress   = 0;
		ChannelBuffer buffer     = ChannelBuffers.buffer((int) len);
		byte[]        tmp        = new byte[4 * 1024];

		InputStream is = entity.getContent();
        while ((chunkSize = is.read(tmp)) != -1)
        {
        	buffer.writeBytes(tmp, 0, chunkSize);
            progress += chunkSize;
            if(listener != null)
            {
            	listener.on(len, progress);
            }
        }

        /* We need to read and replace the stream while the socket is still open */
        byte[]      raw         = buffer.array();
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
