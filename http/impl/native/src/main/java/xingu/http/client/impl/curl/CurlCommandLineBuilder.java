package xingu.http.client.impl.curl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.handler.codec.http.Cookie;

import br.com.ibnetwork.xingu.lang.NotImplementedYet;
import xingu.http.client.HttpRequest;
import xingu.http.client.NameValue;
import xingu.http.client.impl.CommandLineBuilderSupport;

/*
 * See: http://curl.haxx.se/docs/manpage.html
 */
public class CurlCommandLineBuilder
	extends CommandLineBuilderSupport
{
	@Override
	public String name()
	{
		return "curl";
	}

	@Override
	public List<String> buildLine(HttpRequest req, File file)
	{
		List<String> result = new ArrayList<String>();
		result.add("curl");
		result.add("-v");
		result.add("-i");
		result.add("-o");
		result.add(file.toString());
		

		String certificate = req.getCertificate();
		if(StringUtils.isNotEmpty(certificate))
		{
			result.add("--cert");
			result.add(certificate);
		}

		placeCookies(req, 	result);
		placeUserAgent(req, result);
		placeHeaders(req, 	result);

		if(req.isPost())
		{
			placePostFields(req, result);
		}
		else
		{
			placeQueryStringFields(req, result);
		}

		result.add("--compressed");
		
		return result;
	}

	private void placePostFields(HttpRequest req, List<String> result)
	{
		if(req.isMultipart())
		{
			placeMultipartFields(req, result);
		}
		else
		{
			placeDataFields(req, result);
		}
		result.add(req.getUri());
	}

	private void placeMultipartFields(HttpRequest req, List<String> result)
	{
		// TODO: try to work with only one list
		List<NameValue> uploadFields = req.getUploadFiles();
		int len = uploadFields == null ? 0 : uploadFields.size();
		if(len > 0)
		{
			for(NameValue f : uploadFields)
			{
				result.add("-F");
				result.add(f.getName() + "=@'" + f.getValue() + "'");
			}
		}

		List<NameValue> fields = req.getFields();
		int len2 = fields == null ? 0 : fields.size();
		if(len2 > 0)
		{
			for(NameValue f : fields)
			{
				result.add("-F");
				result.add(f.getName() + "='" + f.getValue() + "'");
			}
		}
	}

	private void placeUserAgent(HttpRequest req, List<String> result)
	{
		String ua = req.getUserAgent();
		if(StringUtils.isNotEmpty(ua))
		{
			result.add("--user-agent");
			result.add(ua);
		}
	}

	private void placeHeaders(HttpRequest req, List<String> result)
	{
		List<NameValue> fields = req.getHeaders();
		int len = fields == null ? 0 : fields.size();
		if(len > 0)
		{
			for(NameValue f : fields)
			{
				result.add("-H");
				result.add(f.getName() + ": " + f.getValue());
			}
		}
	}

	private void placeQueryStringFields(HttpRequest req, List<String> result)
	{
		List<NameValue> fields = req.getFields();
		int len = fields == null ? 0 : fields.size();
		StringBuffer uri = new StringBuffer(req.getUri());

		if(len > 0)
		{
			int i = 0;
			uri.append("?");
			for(NameValue f : fields)
			{
				i++;
				uri.append(f.getName()).append("=").append(f.getValue());
				if(i < len)
				{
					uri.append("&");
				}
			}
		}
		result.add(uri.toString());
	}

	private void placeDataFields(HttpRequest req, List<String> result)
	{
		List<NameValue> fields = req.getFields();
		int len = fields == null ? 0 : fields.size();

		if(len > 0)
		{
			int i = 0;

			result.add("--data");
			StringBuffer sb = new StringBuffer();
			for(NameValue f : fields)
			{
				i++;
				sb.append(f.getName()).append("=").append(f.getValue());
				if(i < len)
				{
					sb.append("&");
				}
			}
			result.add(sb.toString());
		}
	}

	private void placeCookies(HttpRequest req, List<String> result)
	{
		List<Cookie> cookies = req.getCookies();
		int len = cookies == null ? 0 : cookies.size();
		if(len > 0)
		{
			int i = 0;
			result.add("--cookie");
			StringBuffer sb = new StringBuffer();
			for(Cookie c : cookies)
			{
				if(c == null 
						|| StringUtils.isEmpty(c.getName())
						|| StringUtils.isEmpty(c.getValue()))
				{
					throw new NotImplementedYet("Cookie is null");
				}
				i++;
				sb.append(c.getName()).append("=").append(c.getValue());
				
				if(i < len)
				{
					sb.append("; ");
				}
			}
			result.add(sb.toString());
		}
	}
}