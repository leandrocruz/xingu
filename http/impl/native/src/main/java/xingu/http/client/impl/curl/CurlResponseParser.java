package xingu.http.client.impl.curl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.buffer.ChannelBuffers;

import xingu.http.client.HttpResponse;
import xingu.http.client.NameValue;
import xingu.http.client.impl.HttpResponseImpl;
import xingu.http.client.impl.NameValueImpl;
import xingu.utils.NettyUtils;
import xingu.utils.NumberUtils;

public class CurlResponseParser
{
	private static final NameValue[] EMPTY = new NameValue[]{};

	private static int toResponseCode(String line)
	{
		String[] parts = StringUtils.split(line, " ");
		return NumberUtils.toInt(parts[1], -1);
	}

	private static NameValue toHeader(String line)
	{
		int    idx   = line.indexOf(":");
		String name  = line.substring(0, idx);
		String value = line.substring(idx + 1);
		name         = StringUtils.trimToEmpty(name);
		value        = StringUtils.trimToEmpty(value);
		return new NameValueImpl(name, value);
	}

	public static HttpResponse responseFrom(String uri, File file)
		throws Exception
	{
		InputStream is = new FileInputStream(file);
		return responseFrom(uri, is, false);
	}
	
	public static HttpResponse responseFrom(String uri, File file, boolean withProxy)
		throws Exception
	{
		InputStream is = new FileInputStream(file);
		return responseFrom(uri, is, withProxy);
	}

	private static void removeProxyHeaders(ChannelBuffer buffer)
	{
		String line = "";
		
		do
		{
			line = NettyUtils.readLine(buffer);
		}
		while(StringUtils.isNotEmpty(line));
	}

	private static HttpResponse parseResult(String uri, ChannelBuffer buffer, int code, List<NameValue> headers)
	{
		HttpResponseImpl result = new HttpResponseImpl();
		result.setCode(code);
		result.setHeaders(headers.toArray(EMPTY));
		result.setUri(uri);

		ChannelBufferInputStream raw = new ChannelBufferInputStream(buffer);
		result.setRawBody(raw);
		return result;
	}
	
	public static HttpResponse responseFrom(String uri, InputStream is)
		throws Exception
	{
		return responseFrom(uri, is, false);
	}
	
	public static HttpResponse responseFrom(String uri, InputStream is, boolean withProxy)
		throws Exception
	{
		byte[]        data   = IOUtils.toByteArray(is);
		ChannelBuffer buffer = ChannelBuffers.wrappedBuffer(data);
		IOUtils.closeQuietly(is);

		/* Parsing State */
		int    i    = 0;
		int    code = -1;
		String line = null;
		
		if(withProxy)
		{
			removeProxyHeaders(buffer);
		}
		
		List<NameValue> headers = new ArrayList<NameValue>();
		do
		{
			line = NettyUtils.readLine(buffer);
			
			if(i == 0)
			{
				code = toResponseCode(line);
			}
			else
			{
				if(StringUtils.isEmpty(line))
				{
					if(code == 100)
					{
						/* HTTP CONTINUE */
						i = 0;
						
						continue;
					}
				}
				else
				{
					NameValue h = toHeader(line);
					headers.add(h);
				}
			}
			i++;
		}
		while(StringUtils.isNotEmpty(line) || code == 100);
		
		return parseResult(uri, buffer, code, headers);
	}
}