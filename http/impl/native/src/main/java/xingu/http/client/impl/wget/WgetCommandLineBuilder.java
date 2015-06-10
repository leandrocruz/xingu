package xingu.http.client.impl.wget;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.handler.codec.http.Cookie;

import xingu.http.client.CommandLineBuilder;
import xingu.http.client.Cookies;
import xingu.http.client.HttpRequest;
import xingu.http.client.HttpResponse;
import xingu.http.client.NameValue;
import xingu.lang.NotImplementedYet;

public class WgetCommandLineBuilder
	implements CommandLineBuilder
{
	@Override
	public String name()
	{
		return "wget";
	}

	@Override
	public List<String> buildLine(HttpRequest req, File file)
	{
		List<String> result = new ArrayList<String>();
		result.add("wget");
		result.add("-d");
		result.add("-S");
		result.add("--save-headers");
		result.add("--keep-session-cookies");
		result.add("-O");
		result.add(file.toString());
		
		String certificate = req.getCertificate();
		if(StringUtils.isNotEmpty(certificate))
		{
			result.add("--certificate");
			result.add(certificate);
		}
		
		placeCookies(req, result);
		placeUserAgent(req, result);
		placeHeaders(req, result);
		placeFields(req, result);

		result.add(req.getUri());
		return result;
	}

	private void placeUserAgent(HttpRequest req, List<String> result)
	{
		result.add("--user-agent='" + req.getUserAgent() + "'");
	}

	private void placeHeaders(HttpRequest req, List<String> result)
	{
		List<NameValue> headers = req.getHeaders();
		if(headers != null && headers.size() > 0)
		{
			for(NameValue h : headers)
			{
				result.add("--header='" + h.getName() + ": " + h.getValue() + "'");
			}
		}
	}

	private void placeFields(HttpRequest req, List<String> result)
	{
		List<NameValue> fields = req.getFields();
		int             len    = fields.size();
		if(fields != null && len > 0)
		{
			boolean isPost = req.isPost();
			if(isPost)
			{
				result.add("--post-data");
				StringBuffer sb = new StringBuffer("'");
				int i = 0;
				for(NameValue f : fields)
				{
					i++;
					sb.append(f.getName()).append("=").append(f.getValue());
					if(i < len)
					{
						sb.append("&");
					}
				}
				sb.append("'");
				result.add(sb.toString());
			}
			else
			{
				throw new NotImplementedYet();
			}
		}
	}

	private void placeCookies(HttpRequest req, List<String> result)
	{
		Cookies cookies = req.getCookies();
		if(cookies != null && cookies.getBuffer().size() > 0)
		{
			for(Cookie c : cookies.getBuffer())
			{
				result.add("--header='Cookie: " + c.getName() + "=" + c.getValue() + "'");
			}
		}
	}

	@Override
	public HttpResponse responseFrom(HttpRequest req, File file)
		throws Exception
	{
		throw new NotImplementedYet();
	}
	
	@Override
	public String join(List<String> cmd)
	{
		return StringUtils.join(cmd, " ");
	}
}