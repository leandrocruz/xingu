package xingu.http.client.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.buffer.ChannelBuffers;

import xingu.http.client.HttpRequest;
import xingu.http.client.HttpResponse;
import xingu.http.client.NameValue;
import xingu.utils.NettyUtils;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.NumberUtils;

public abstract class CommandLineBuilderSupport
	implements CommandLineBuilder
{
	private static final NameValue[] EMPTY = new NameValue[]{};
	
	@Override
	public List<String> buildLine(HttpRequest request, File file)
			throws UnsupportedEncodingException
	{
		throw new NotImplementedYet();
	}

	@Override
	public HttpResponse responseFrom(HttpRequest req, File file)
		throws Exception
	{
		InputStream   is     = new FileInputStream(file);
		byte[]        data   = IOUtils.toByteArray(is);
		ChannelBuffer buffer = ChannelBuffers.wrappedBuffer(data);

		/* Parsing State */
		int    i    = 0;
		int    code = -1;
		String line = null;
		
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
		
		HttpResponseImpl result = new HttpResponseImpl();
		result.setCode(code);
		result.setHeaders(headers.toArray(EMPTY));
		result.setUri(req.getUri());

		ChannelBufferInputStream raw = new ChannelBufferInputStream(buffer);
		result.setRawBody(raw);
		return result;
	}

	private static int toResponseCode(String line)
	{
		String[] parts = StringUtils.split(line, " ");
		return NumberUtils.toInt(parts[1], -1);
	}

	private static NameValue toHeader(String line)
	{
		int 	idx 	= line.indexOf(":");
		String 	name 	= line.substring(0, idx);
		String 	value 	= line.substring(idx + 1);		
		name  			= StringUtils.trimToEmpty(name);
		value 			= StringUtils.trimToEmpty(value);		
		return new NameValueImpl(name, value);
	}
}