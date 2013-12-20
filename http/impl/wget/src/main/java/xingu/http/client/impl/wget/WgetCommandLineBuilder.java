package xingu.http.client.impl.wget;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jboss.netty.handler.codec.http.Cookie;

import xingu.http.client.HttpRequest;
import xingu.http.client.NameValue;
import xingu.http.client.impl.CommandLineBuilderSupport;
import br.com.ibnetwork.xingu.lang.NotImplementedYet;

public class WgetCommandLineBuilder
	extends CommandLineBuilderSupport
{
	@Override
	public String name()
	{
		return "wget";
	}

	@Override
	public String buildLine(HttpRequest req, File file)
	{

		StringBuffer buffer = new StringBuffer();
		buffer
			.append("wget -d -S --save-headers --keep-session-cookies -O ")
			.append(file);
		
		String certificate = req.getCertificate();
		if(StringUtils.isNotEmpty(certificate))
		{
			buffer.append(" --certificate ").append(certificate);
		}
		
		placeCookies(req, buffer);
		placeUserAgent(req, buffer);
		placeHeaders(req, buffer);
		placeFields(req, buffer);

		buffer.append(" ").append(req.getUri());
		return buffer.toString();
	}

	private void placeUserAgent(HttpRequest req, StringBuffer buffer)
	{
		buffer.append(" --user-agent='").append(req.getUserAgent()).append("'");
	}

	private void placeHeaders(HttpRequest req, StringBuffer buffer)
	{
		List<NameValue> headers = req.getHeaders();
		if(headers != null && headers.size() > 0)
		{
			for(NameValue h : headers)
			{
				buffer
				.append(" --header='")
				.append(h.getName())
				.append(": ")
				.append(h.getValue())
				.append("'");
			}
		}
	}
	

	private void placeFields(HttpRequest req, StringBuffer buffer)
	{
		List<NameValue> fields = req.getFields();
		int             len    = fields.size();
		if(fields != null && len > 0)
		{
			boolean isPost = req.isPost();
			if(isPost)
			{
				buffer.append(" --post-data '");
				int i = 0;
				for(NameValue f : fields)
				{
					buffer.append(f.getName()).append("=").append(f.getValue());
					i++;
					if(i < len)
					{
						buffer.append("&");
					}
				}
				buffer.append("'");
			}
			else
			{
				throw new NotImplementedYet();
			}
		}
	}

	private void placeCookies(HttpRequest req, StringBuffer buffer)
	{
		List<Cookie> cookies = req.getCookies();
		if(cookies != null && cookies.size() > 0)
		{
			for(Cookie c : cookies)
			{
				buffer
					.append("--header='Cookie: ")
					.append(c.getName())
					.append("=")
					.append(c.getValue())
					.append("'");
			}
		}
	}
}