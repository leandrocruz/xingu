package xingu.http.client.impl.curl;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import xingu.http.client.HttpResponse;
import xingu.http.client.NameValue;
import xingu.http.client.impl.HttpResponseImpl;
import xingu.http.client.impl.NameValueImpl;
import xingu.utils.NumberUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.stream.Collectors;

public class CurlResponseParser
{
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

	public static HttpResponse responseFrom(String uri, File bodyFile, File headersFile)
		throws Exception
	{
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new FileReader(headersFile));
			int code = toResponseCode(reader.readLine());
			List<NameValue> headers = reader.lines().skip(1).filter(StringUtils::isNotBlank).map(CurlResponseParser::toHeader).collect(Collectors.toList());

			HttpResponseImpl result = new HttpResponseImpl();
			result.setCode(code);
			result.setUri(uri);
			result.setHeaders(headers);
			result.setBody(bodyFile);
			return result;
		}
		finally
		{
			IOUtils.closeQuietly(reader);
		}
	}
}