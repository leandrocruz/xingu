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
import java.util.Collections;
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
		return new NameValueImpl(StringUtils.trimToEmpty(name), StringUtils.trimToEmpty(value));
	}

	private static HttpResponseImpl parseHeaders(File file)
		throws Exception
	{
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new FileReader(file));
			List<String>    lines 	 = reader.lines().filter(StringUtils::isNotBlank).collect(Collectors.toList());
			List<Integer>   statuses = lines.stream().filter(l ->  l.startsWith("HTTP")).map(CurlResponseParser::toResponseCode).collect(Collectors.toList());
			List<NameValue> headers  = lines.stream().filter(l -> !l.startsWith("HTTP")).map(CurlResponseParser::toHeader).collect(Collectors.toList());

			// Sometimes we have more that one result (http 100, for example). Get the last one
			Collections.sort(statuses, Collections.reverseOrder());
			int code = statuses.get(0);

			HttpResponseImpl result = new HttpResponseImpl();
			result.setCode(code);
			result.setHeaders(headers);
			return result;
		}
		finally
		{
			IOUtils.closeQuietly(reader);
		}
	}

	public static HttpResponse responseFrom(String uri, File body, File headers)
		throws Exception
	{
		if(headers == null)
			throw new IllegalArgumentException("Missing headers for '"+uri+"'. Please check '"+body+"'");

		HttpResponseImpl result = parseHeaders(headers);
		result.setUri(uri);
		result.setBody(body);

		return result;
	}
}