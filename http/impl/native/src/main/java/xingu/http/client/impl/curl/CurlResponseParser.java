package xingu.http.client.impl.curl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.Charsets;
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

	@SuppressWarnings("resource")
	// Note: suppressed because it's being closed in responseFrom.
	// Note: there's a reason for this that Jonas doesn't remember.
	public static HttpResponse responseFrom(String uri, File file)
		throws Exception
	{
		InputStream is = new FileInputStream(file);
		try
		{
			return responseFrom(uri, is);
		}
		catch(Exception ex)
		{
			is = new FileInputStream(file);
			return responseFrom(uri, is);
		}
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
		/* Parsing State */
		int    i    = 0;
		int    code = -1;
		String line = null;
		
		List<NameValue> headers = new ArrayList<NameValue>();
		
		String lines = IOUtils.toString(is);
		String clean = lines.replaceAll("(HTTP\\/\\d\\.\\d \\d+ [^$]+)(?=HTTP\\/)", "");
		IOUtils.closeQuietly(is);
		ChannelBuffer buff = ChannelBuffers.wrappedBuffer(clean.getBytes());
		
		do
		{
			line = NettyUtils.readLine(buff);
			
			if(i == 0)
			{
				code = toResponseCode(line);
			}
			else if(!StringUtils.isEmpty(line))
			{
				NameValue h = toHeader(line);
				headers.add(h);
			}
			i++;
		}
		while(StringUtils.isNotEmpty(line));
		
		return parseResult(uri, buff, code, headers);
	}
}