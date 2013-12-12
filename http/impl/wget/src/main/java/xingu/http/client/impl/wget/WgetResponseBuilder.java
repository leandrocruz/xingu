package xingu.http.client.impl.wget;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import xingu.http.client.Header;
import xingu.http.client.HttpResponse;
import xingu.http.client.impl.HeaderImpl;
import br.com.ibnetwork.xingu.utils.NumberUtils;

public class WgetResponseBuilder
{
	public static HttpResponse<String> buildFrom(String uri, File file, byte[] byteArray)
		throws IOException
	{
		List<Header> headers = new ArrayList<Header>();
		String       s       = new String(byteArray);
		List<String> lines   = IOUtils.readLines(new StringReader(s));
		int          i       = 0;
		int          code    = -1;
		for(String line : lines)
		{
			if(i == 0)
			{
				code = toResponseCode(line);
			}
			else
			{
				Header h = toHeader(line);
				headers.add(h);
			}
			i++;
		}
		
		WgetResponse<String> result = new WgetResponse<String>();
		result.setFile(file);
		result.setCode(code);
		result.setHeaders(headers.toArray(new Header[]{}));
		result.setUri(uri);

		InputStream is   = new FileInputStream(file);
		String      body = IOUtils.toString(is);
		result.setBody(body);
		IOUtils.closeQuietly(is);
		
		result.setRawBody(new FileInputStream(file));
		return result;
	}
	
	private static int toResponseCode(String line)
	{
		String[] parts = StringUtils.split(line, " ");
		return NumberUtils.toInt(parts[1], -1);
	}

	private static Header toHeader(String line)
	{
		String[] parts = StringUtils.split(line, ":");
		String   name  = StringUtils.trimToEmpty(parts[0]);
		String   value = StringUtils.trimToEmpty(parts[1]);
		return new HeaderImpl(name, value);
	}
}
