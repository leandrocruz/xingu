package xingu.http.client.impl;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import br.com.ibnetwork.xingu.utils.NumberUtils;
import xingu.http.client.HttpRequest;
import xingu.http.client.HttpResponse;
import xingu.http.client.NameValue;

public class CommandLineBuilderSupport
	implements CommandLineBuilder
{
	private static final NameValue[] EMPTY = new NameValue[]{};
	
	@Override
	public String buildLine(HttpRequest request, File file)
	{
		throw new NotImplementedYet();
	}

	@Override
	public <T> HttpResponse<T> responseFrom(HttpRequest req, File file)
		throws Exception
	{
		/* Parsing State */
		int     i              = 0;
		int     code           = -1;
		boolean readingHeaders = true;

		StringBuffer    body           = new StringBuffer();
		List<NameValue> headers        = new ArrayList<NameValue>();
		Reader          reader         = new FileReader(file);
		LineIterator    it             = IOUtils.lineIterator(reader);
		while(it.hasNext())
		{
			String line = it.next();
			if(i == 0)
			{
				code = toResponseCode(line);
			}
			else if(readingHeaders)
			{
				if(StringUtils.isEmpty(line))
				{
					if(code == 100)
					{
						/* HTTP CONTINUE */
						i = 0;
						continue;
					}
					readingHeaders = false;
					continue;
				}
				else
				{
					NameValue h = toHeader(line);
					headers.add(h);
				}
			}
			else
			{
				body.append(line);
			}
			i++;
		}
		
		SimpleHttpResponse<T> result = new SimpleHttpResponse<T>();
		result.setCode(code);
		result.setHeaders(headers.toArray(EMPTY));
		result.setUri(req.getUri());
		result.setBody(body.toString());
		result.setRawBody(IOUtils.toInputStream(body));
		return result;
	}

	private static int toResponseCode(String line)
	{
		String[] parts = StringUtils.split(line, " ");
		return NumberUtils.toInt(parts[1], -1);
	}

	private static NameValue toHeader(String line)
	{
		String[] parts = StringUtils.split(line, ":");
		String   name  = StringUtils.trimToEmpty(parts[0]);
		String   value = StringUtils.trimToEmpty(parts[1]);
		return new NameValueImpl(name, value);
	}
}